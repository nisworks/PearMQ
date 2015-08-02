# PearMQ - Message Queue peer-to-peer alternative
PearMQ is an alternative to traditional JMS and MQ middleware infrastructure. It combines simplicity of REST web services with performance and managability of JMS without a need of additional middleware infrastructure.

## Why peer-to-peer?
Tradditionally computer systems communicate in client-server model. This is the most natural way off communicating between a large scale system and a customer consuming its services. But now, large scale systems are nolonger monolithic beasts - they are orchestrated swarms of microservices. Instead of client-server, we have groups of peers providing and consuming each other's services. This situation makes traditional Meb Services and Message Queues cumbersome and inconvinient. The most natural model for communicating independent intelligent agents is peer-to-peer, and all gouvernance necessary can be built into these agents self-organizing behavioural patterns.


## Loadbalancing

Ability to horizontally scale services is one of the most important feature in SOA. The usual approach is the use of a designater mediator to equally balance traffic between service instances. The mediator itself cannot scale other than vertically, and becomes single point of failure - the very thing we want to avoid. The solution is to make customers aware they are dealing with multiple service instances and split traffic between all of them.

###Round Robin
Easiest approach - client is equally splitting traffic between all of the registered service instances.

###Stohastic Weigted Mediator
Client is collecting response time statistics of each service instance and than roll the dice to choose instance for next invocation. Instances with shortest response times by assumption are the most performant and hence get proporionally more load.

###Deterministic mediator
In a very common example there is a requirement that singe service should support each of the session - equivalent of sticky session flag. In this case load balancing algorythm can be based on session identifier hash - making sure that services are assigned in deterministic way. In case of add/remove service instance, mediator will split traffic in equal and deterministic way.

## Autodiscovery

With Goosip Algorythm, peers can exchange information about services provided by each instance, and dynamically allocate services provided by each new connected instance.

## Zookeeper integration

To make clients independant of service URL changes, peers can be integrated with Apache Zookeeper, making it easy to dynamically scale service instances.

## Security

PearMQ integrates with Apache Keycloak to provide service authentication and autorization.

## Persistance

In peer-to-peer model persistane by MQ means is not necessary, both peers know immedietlay if communication was successfull and can react. In case of communication problem on either side, lost message can be stored in persistant log file, giving ability to troubleshoot and to retry once service is availible.

## Synchronic and asynchronic channels
By default service calls are syncronic, but asynchronic calls with Callback design pattern are also supperted.

## Throtteling
Each service can have trottling configured for maximum number of conncurrent service calls, protecting service instance from overloading by massive flood of concurrent calls.

## Examples
