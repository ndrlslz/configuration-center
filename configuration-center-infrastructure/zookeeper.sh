work_dir=$(dirname $0)
active_zookeeper_id=$(docker-compose -f ${work_dir}/zookeeper.yml ps -q)

case "$1" in
    status)
        docker-compose -f ${work_dir}/zookeeper.yml ps
    ;;
    start)
        if [ -n "${active_zookeeper_id}" ]; then
            echo "zookeeper has already been started"
        else
            docker-compose -f ${work_dir}/zookeeper.yml up -d
        fi
    ;;
    stop)
        if [ -n "${active_zookeeper_id}" ]; then
            echo "stop zookeeper"
            docker-compose -f ${work_dir}/zookeeper.yml down
        else
            echo "zookeeper has already been stopped"
        fi
    ;;
    restart)
        echo "restart zookeeper"
        docker-compose -f ${work_dir}/zookeeper.yml restart
    ;;
    *)
        echo "Usage: ./zookeeper.sh start|stop|restart"
esac