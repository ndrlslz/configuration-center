package com.ndrlslz.configuration.center.spring.configuration;

import com.ndrlslz.configuration.center.sdk.client.ConfigurationTemplate;
import com.ndrlslz.configuration.center.spring.annotation.Config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.nonNull;

@Configuration
public class ConfigAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter
        implements PriorityOrdered, BeanFactoryAware {

    private final Log logger = LogFactory.getLog(getClass());

    private Class<Config> configAnnotationType = Config.class;

    private static final int order = Ordered.LOWEST_PRECEDENCE - 3;

    private final Map<String, InjectionMetadata> injectionMetadataCache =
            new ConcurrentHashMap<>(256);

    private ConfigurableListableBeanFactory beanFactory;

    private ConfigurationTemplate configurationTemplate;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "AutowiredAnnotationBeanPostProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
        this.configurationTemplate = beanFactory.getBean(ConfigurationTemplate.class);
    }

    @Override
    public PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        InjectionMetadata metadata = findConfigMetadata(beanName, bean.getClass(), pvs);
        try {
            metadata.inject(bean, beanName, pvs);
        } catch (BeanCreationException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new BeanCreationException(beanName, "Injection of configuration property failed", ex);
        }
        return pvs;
    }

    @Override
    public int getOrder() {
        return order;
    }

    private InjectionMetadata findConfigMetadata(String beanName, Class<?> clazz, PropertyValues pvs) {
        // Fall back to class name as cache key, for backwards compatibility with custom callers.
        String cacheKey = (StringUtils.hasLength(beanName) ? beanName : clazz.getName());
        // Quick check on the concurrent map first, with minimal locking.
        InjectionMetadata metadata = this.injectionMetadataCache.get(cacheKey);
        if (InjectionMetadata.needsRefresh(metadata, clazz)) {
            synchronized (this.injectionMetadataCache) {
                metadata = this.injectionMetadataCache.get(cacheKey);
                if (InjectionMetadata.needsRefresh(metadata, clazz)) {
                    if (metadata != null) {
                        metadata.clear(pvs);
                    }
                    try {
                        metadata = buildConfigMetadata(clazz);
                        this.injectionMetadataCache.put(cacheKey, metadata);
                    } catch (NoClassDefFoundError err) {
                        throw new IllegalStateException("Failed to introspect bean class [" + clazz.getName() +
                                "] for config metadata: could not find class that it depends on", err);
                    }
                }
            }
        }
        return metadata;
    }

    private AnnotationAttributes findConfigAnnotation(AccessibleObject ao) {
        if (ao.getAnnotations().length > 0) {
            AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, configAnnotationType);
            if (attributes != null) {
                return attributes;
            }
        }
        return null;
    }

    private InjectionMetadata buildConfigMetadata(final Class<?> clazz) {
        LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<>();
        Class<?> targetClass = clazz;

        do {
            final LinkedList<InjectionMetadata.InjectedElement> currElements =
                    new LinkedList<>();

            ReflectionUtils.doWithLocalFields(targetClass, field -> {
                AnnotationAttributes ann = findConfigAnnotation(field);
                if (nonNull(ann)) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("Config annotation is not supported on static fields: " + field);
                        }
                        return;
                    }
                    currElements.add(new ConfigFieldElement(field));
                }
            });

            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);

        return new InjectionMetadata(clazz, elements);
    }

    private class ConfigFieldElement extends InjectionMetadata.InjectedElement {
        ConfigFieldElement(Field field) {
            super(field, null);
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
            Field field = (Field) this.member;
            String value;
            if (field.isAnnotationPresent(configAnnotationType)) {
                Config annotation = field.getAnnotation(configAnnotationType);
                String specifiedName = annotation.value();
                if (specifiedName.isEmpty()) {
                    String name = field.getName();
                    value = configurationTemplate.get(name);
                } else {
                    value = configurationTemplate.get(specifiedName);
                }
                ReflectionUtils.makeAccessible(field);

                Object result = convert(field, value);
                field.set(bean, result);
            }
        }

        private Object convert(Field field, String value) {
            Object result;
            Class<?> type = field.getType();
            if (type == String.class) {
                result = value;
            } else if (type == Integer.class || type == int.class) {
                result = Integer.valueOf(value);
            } else if (type == Boolean.class || type == boolean.class) {
                result = Boolean.valueOf(value);
            } else {
                throw new RuntimeException("type not supported");
            }
            return result;
        }
    }
}