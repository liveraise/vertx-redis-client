/**
 * = Vert.x-redis
 * :toc: left
 *
 * Vert.x-redis is redis client to be used with Vert.x.
 *
 * This module allows data to be saved, retrieved, searched for, and deleted in a Redis. Redis is an open source, BSD
 * licensed, advanced key-value store. It is often referred to as a data structure server since keys can contain
 * strings, hashes, lists, sets and sorted sets. To use this module you must have a Redis server instance running on
 * your network.
 *
 * Redis has a rich API and it can be organized in the following groups:
 *
 * * Cluster - Commands related to cluster management, note that using most of these commands you will need a redis server with version &gt;=3.0.0
 * * Connection - Commands that allow you to switch DBs, connect, disconnect and authenticate to a server.
 * * Hashes - Commands that allow operations on hashes.
 * * HyperLogLog - Commands to approximating the number of distinct elements in a multiset, a HyperLogLog.
 * * Keys - Commands to work with Keys.
 * * List - Commands to work with Lists.
 * * Pub/Sub - Commands to create queues and pub/sub clients.
 * * Scripting - Commands to run Lua Scripts in redis.
 * * Server - Commands to manage and get server configurations.
 * * Sets - Commands to work with un ordered sets.
 * * Sorted Sets - Commands to work with sorted sets.
 * * Strings - Commands to work with Strings.
 * * Transactions - Commands to handle transaction lifecycle.
 *
 * == Using Vert.x-Redis
 *
 * To use the Vert.x Redis client, add the following dependency to the _dependencies_ section of your build descriptor:
 *
 * * Maven (in your `pom.xml`):
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 * <groupId>${maven.groupId}</groupId>
 * <artifactId>${maven.artifactId}</artifactId>
 * <version>${maven.version}</version>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * compile '${maven.groupId}:${maven.artifactId}:${maven.version}'
 * ----
 *
 * == Connecting to Redis
 *
 * In order to connect to Redis there is a config required. This config is provided in the form of {@link io.vertx.redis.RedisOptions}
 * containing the following values:
 *
 * * `host`: default is `localhost`
 * * `port`: default is `6379`
 * * `encoding`: default is `UTF-8`
 * * `tcpKeepAlive`: default `true`
 * * `tcpNoDelay`: default `true`
 *
 * An connection example can then be:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example1}
 * ----
 *
 * The client attempts to reconnect to the server on connection errors, for this reason if you are connecting to a server
 * that requires authentication and/or you are not using the default database you must provide the authentication
 * password and/or database id to the config object, the properties names are:
 *
 * * `auth`
 * * `select`
 *
 * If you do not do this and manually call the {@link io.vertx.redis.RedisClient#auth(java.lang.String, io.vertx.core.Handler)}
 * or {@link io.vertx.redis.RedisClient#select(int, io.vertx.core.Handler)} then the client will not know how to recover
 * the connection in case of socket error.
 *
 * == Running commands
 *
 * Given that the redis client is connected to the server, all commands are now possible to execute using this module.
 * The module offers a clean API for executing commands without the need to hand write the command itself, for example
 * if one wants to get a value of a key it can be done as:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example2}
 * ----
 *
 * In order to know more about the commands available you should look at: <a href="http://redis.io/commands">redis documentation</a>.
 *
 * == Pub/Sub mode
 *
 * Redis supports queues and pub/sub mode, when operated in this mode once a connection invokes a subscriber mode then
 * it cannot be used for running other commands than the command to leave that mode.
 *
 * To start a subscriber one would do:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example3}
 * ----
 *
 * And from another place in the code publish messages to the queue:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#example4}
 * ----
 *
 * == Friendlier hash commands
 *
 * Most Redis commands take a single String or an Array of Strings as arguments, and replies are sent back as a single
 * String or an Array of Strings. When dealing with hash values, there are a couple of useful exceptions to this.
 *
 * === Command hgetall
 *
 * The reply from an hgetall command will be converted into a JSON Object. That way you can interact with the responses
 * using JSON syntax which is handy for the EventBus communication.
 *
 * === command mset
 *
 * Multiple values in a hash can be set by supplying an object. Note however that key and value will be coerced to
 * strings.
 *
 * ----
 * {
 * keyName: "value",
 * otherKeyName: "other value"
 * }
 * ----
 *
 * === command msetnx
 *
 * Multiple values in a hash can be set by supplying an object. Note however that key and value will be coerced to
 * strings.
 *
 * ----
 * {
 * keyName: "value",
 * otherKeyName: "other value"
 * }
 * ----
 *
 * === command hmset
 *
 * Multiple values in a hash can be set by supplying an object. Note however that key and value will be coerced to
 * strings.
 *
 * ----
 * {
 * keyName: "value",
 * otherKeyName: "other value"
 * }
 * ----
 *
 * === command zadd
 * Multiple values in a hash can be set by supplying an object. Note however that key and value will be coerced to
 * strings.
 *
 * ----
 * {
 * score: "member",
 * otherScore: "other member"
 * }
 * ----
 *
 * == Server Info
 *
 * In order to make it easier to work with the info response you don't need to parse the data yourself and the module
 * will return it in a easy to understand JSON format. The format is as follows: A JSON object for each section filled
 * with properties that belong to that section. If for some reason there is no section the properties will be visible
 * at the top level object.
 *
 * ----
 * {
 * server: {
 * redis_version: "2.5.13",
 * redis_git_sha1: "2812b945",
 * redis_git_dirty: "0",
 * os: "Linux 2.6.32.16-linode28 i686",
 * arch_bits: "32",
 * multiplexing_api: "epoll",
 * gcc_version: "4.4.1",
 * process_id: "8107",
 * ...
 * },
 * memory: {...},
 * client: {...},
 * ...
 * }
 * ----
 *
 * == Eval and Evalsha
 *
 * Eval and Evalsha commands are special due to its return value can be any type. Vert.x is built on top of Java and the
 * language adheres to strong typing so returning any type turns to be problematic since we want to avoid having `Object`
 * type being used. The reason to avoid the type `Object` is that we also are polyglot and the conversion between
 * languages would become rather complicated and hard to implement. For all these reasons the commands eval and evalsha
 * will always return a JsonArray, even for example for scripts such as:
 *
 * ```
 * return 10
 * ```
 *
 * In this case the return value will be a json array with the value 10 on index 0.
 *
 * == Redis Sentinel
 *
 * This client support the Redis Sentinel API with the API interface:
 * {@link io.vertx.redis.sentinel.RedisSentinel}.
 *
 * The API exposes the sentinel commands:
 *
 * * masters
 * * master
 * * slaves
 * * sentinels
 * * get-master-addr-by-name
 * * reset
 * * failover
 * * ckquorum
 * * flushconfig
 *
 * For more information please read the redis official documentation: https://redis.io/topics/sentinel
 *
 */
@ModuleGen(name = "vertx-redis", groupPackage = "io.vertx")
@Document(fileName = "index.adoc")
package io.vertx.redis;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
