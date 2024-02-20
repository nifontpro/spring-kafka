docker compose -f kafka1kd.yaml up

Получить список топиков
```shell
docker exec -ti broker /usr/bin/kafka-topics --list --bootstrap-server broker:9092
```

Отправить сообщение
```shell
docker exec -ti broker /usr/bin/kafka-console-producer --topic topic1 --bootstrap-server broker:9092
```

Получить сообщения
```shell
docker exec -ti broker /usr/bin/kafka-console-consumer --topic topic1 --bootstrap-server broker:9092 
```

Получить сообщения consumer1
```shell
docker exec -ti kafka /usr/bin/kafka-console-consumer --group consumer1 --topic topic1 --bootstrap-server kafka:9092 
```

Отправить сообщение c ключом через двоеточие (key:value)
```shell
docker exec -ti kafka /usr/bin/kafka-console-producer --topic topic1 --property parse.key=true --property key.separator=: --bootstrap-server kafka:9092
```

Получить сообщения
```shell
docker exec -ti kafka /usr/bin/kafka-console-consumer --topic topic1 --property print.key=true --property print.offset=true --from-beginning --bootstrap-server kafka:9092
```

