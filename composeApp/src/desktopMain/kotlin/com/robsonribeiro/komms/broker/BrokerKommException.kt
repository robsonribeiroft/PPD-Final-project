package com.robsonribeiro.komms.broker

class BrokerQueueAlreadyExistException(queueName: String): Exception("The userId/queue named $queueName already exists!")