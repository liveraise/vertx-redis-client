/**
 * = Vert.x-redis
 * :toc: left
 * <p>
 * Vert.x-redis is redis client to be used with Vert.x.
 * <p>
 * This module allows data to be saved, retrieved, searched for, and deleted in a Redis. Redis is an open source, BSD
 * licensed, advanced key-value store. It is often referred to as a data structure server since keys can contain
 * strings, hashes, lists, sets and sorted sets. To use this module you must have a Redis server instance running on
 * your network.
 * <p>
 * Redis has a rich API and it can be organized in the following groups:
 * <p>
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
 * <p>
 * == Using Vert.x-Redis
 * <p>
 * To use the Vert.x Redis client, add the following dependency to the _dependencies_ section of your build descriptor:
 * <p>
 * * Maven (in your `pom.xml`):
 * <p>
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 * <groupId>${maven.groupId}</groupId>
 * <artifactId>${maven.artifactId}</artifactId>
 * <version>${maven.version}</version>
 * </dependency>
 * ----
 * <p>
 * * Gradle (in your `build.gradle` file):
 * <p>
 * [source,groovy,subs="+attributes"]
 * ----
 * compile '${maven.groupId}:${maven.artifactId}:${maven.version}'
 * ----
 * <p>
 * == Connecting to Redis
 * <p>
 * In order to connect to Redis there is a config required. This config is provided in the form of {@link io.vertx.redis.RedisOptions}
 * containing the following values:
 * <p>
 * * `host`: default is `localhost`
 * * `port`: default is `6379`
 * * `encoding`: default is `UTF-8`
 * * `tcpKeepAlive`: default `true`
 * * `tcpNoDelay`: default `true`
 * <p>
 * An connection example can then be:
 * <p>
 * [source,$lang]
 * ----
 * {@link examples.Examples#example1}
 * ----
 * <p>
 * The client attempts to reconnect to the server on connection errors, for this reason if you are connecting to a server
 * that requires authentication and/or you are not using the default database you must provide the authentication
 * password and/or database id to the config object, the properties names are:
 * <p>
 * * `auth`
 * * `select`
 * <p>
 * If you do not do this and manually call the {@link io.vertx.redis.RedisClient#auth(java.lang.String, io.vertx.core.Handler)}
 * or {@link io.vertx.redis.RedisClient#select(int, io.vertx.core.Handler)} then the client will not know how to recover
 * the connection in case of socket error.
 * <p>
 * == Running commands
 * <p>
 * Given that the redis client is connected to the server, all commands are now possible to execute using this module.
 * The module offers a clean API for executing commands without the need to hand write the command itself, for example
 * if one wants to get a value of a key it can be done as:
 * <p>
 * [source,$lang]
 * ----
 * {@link examples.Examples#example2}
 * ----
 * <p>
 * In order to know more about the commands available you should look at: <a href="http://redis.io/commands">redis documentation</a>.
 * <p>
 * == Pub/Sub mode
 * <p>
 * Redis supports queues and pub/sub mode, when operated in this mode once a connection invokes a subscriber mode then
 * it cannot be used for running other commands than the command to leave that mode.
 * <p>
 * To start a subscriber one would do:
 * <p>
 * [source,$lang]
 * ----
 * {@link examples.Examples#example3}
 * ----
 * <p>
 * And from another place in the code publish messages to the queue:
 * <p>
 * [source,$lang]
 * ----
 * {@link examples.Examples#example4}
 * ----
 * <p>
 * == Friendlier hash commands
 * <p>
 * Most Redis commands take a single String or an Array of Strings as arguments, and replies are sent back as a single
 * String or an Array of Strings. When dealing with hash values, there are a couple of useful exceptions to this.
 * <p>
 * === Command hgetall
 * <p>
 * The reply from an hgetall command will be converted into a JSON Object. That way you can interact with the responses
 * using JSON syntax which is handy for the EventBus communication.
 * <p>
 * === command mset
 * <p>
 * Multiple values in a hash can be set by supplying an object. Note however that key and value will be coerced to
 * strings.
 * <p>
 * ----
 * {
 * keyName: "value",
 * otherKeyName: "other value"
 * }
 * ----
 * <p>
 * === command msetnx
 * <p>
 * Multiple values in a hash can be set by supplying an object. Note however that key and value will be coerced to
 * strings.
 * <p>
 * ----
 * {
 * keyName: "value",
 * otherKeyName: "other value"
 * }
 * ----
 * <p>
 * === command hmset
 * <p>
 * Multiple values in a hash can be set by supplying an object. Note however that key and value will be coerced to
 * strings.
 * <p>
 * ----
 * {
 * keyName: "value",
 * otherKeyName: "other value"
 * }
 * ----
 * <p>
 * === command zadd
 * Multiple values in a hash can be set by supplying an object. Note however that key and value will be coerced to
 * strings.
 * <p>
 * ----
 * {
 * score: "member",
 * otherScore: "other member"
 * }
 * ----
 * <p>
 * == Server Info
 * <p>
 * In order to make it easier to work with the info response you don't need to parse the data yourself and the module
 * will return it in a easy to understand JSON format. The format is as follows: A JSON object for each section filled
 * with properties that belong to that section. If for some reason there is no section the properties will be visible
 * at the top level object.
 * <p>
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
 * <p>
 * == Eval and Evalsha
 * <p>
 * Eval and Evalsha commands are special due to its return value can be any type. Vert.x is built on top of Java and the
 * language adheres to strong typing so returning any type turns to be problematic since we want to avoid having `Object`
 * type being used. The reason to avoid the type `Object` is that we also are polyglot and the conversion between
 * languages would become rather complicated and hard to implement. For all these reasons the commands eval and evalsha
 * will always return a JsonArray, even for example for scripts such as:
 * <p>
 * ```
 * return 10
 * ```
 * <p>
 * In this case the return value will be a json array with the value 10 on index 0.
 */
@ModuleGen(name = "vertx-redis", groupPackage = "io.vertx")
@Document(fileName = "index.adoc")
package io.vertx.redis;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
