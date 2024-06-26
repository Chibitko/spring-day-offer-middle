# Проблематика

Необходимо реализовать сбор аналитических данных со всех магазинов компании

# Варианты решения

## Вариант 1

    Описание:

        Реализовать синхронизацию баз даных с аналитическим хранилищем используя Debezium
        ![Диаграмма 1](/ADR1.drawio.png)

    Плюсы:
        
        Используется готовое решение
        Не используется ресурс разработчиков, задача решается командами DevOps и DBA
        Легко масштабируется
        
    Минусы:
        
        Нужны компетентные специалисты умеющие работать с Debezium
        Используется ресурс DevOps, который как правило, самый дорогой
        Требует много ресурсов для работы решения (CPU, RAM, FS)
        Раздутый стек технологий

## Вариант 2

    Описание:

        Реализовать синхронизацию баз даных с аналитическим хранилищем, используя шаблоны проектирования
        распределенных приложений Outbox и Inbox, с исбользованием брокера сообщений (RabbitMQ или ArtemisMQ)
        ![Диаграмма 2](/ADR2.drawio.png)

    Плюсы:
        
        Полный контроль над работой синхронизации на стороне разработки
        Легко масштабируется
        Не требуется много ресурсов для работы синхронизации
        Используется более распространенное решение с очередью, которая уже как правило в стеке
        
    Минусы:
        
        Требует больше ресурсов для разработки
        Процесс разработки и развертывания более сложный - многошаговый
        Нужно решать проблемы идемпотентности и комутативности

# Выбранное решение

        Реализовать синхронизацию баз даных с аналитическим хранилищем, используя шаблоны проектирования
        распределенных приложений Outbox и Inbox, с исбользованием брокера сообщений (RabbitMQ или ArtemisMQ)
        
        PS: сделано допущение, что в стек нет Debezium

# Обоснование

    Решение 2
        1) Более прогнозируемео на всех этапах разработки и внедрения
        2) Реализуемо на более ограниченом стеке технологий и с меньшим привлечением команд DevOps и DBA
        3) Не требуется много ресурсов для работы синхронизации