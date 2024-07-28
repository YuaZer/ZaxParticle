package io.github.yuazer.zaxparticle.data.mysql

import taboolib.module.configuration.ConfigFile
import java.sql.Connection
import java.sql.DriverManager

class Database(val config:ConfigFile) {
    private val connection: Connection

    init {
        connection = connect()
        createDatabase()
        createTable()
    }

    private fun connect(): Connection {
        val url = "jdbc:mysql://${config.getString("database.host")}:${config.getString("database.port")}/?useSSL=false"
        return DriverManager.getConnection(url, config.getString("database.user"), config.getString("database.password"))
    }

    private fun createDatabase() {
        val sql = "CREATE DATABASE IF NOT EXISTS `${config.getString("database.database")}`"
        connection.createStatement().use { statement ->
            statement.executeUpdate(sql)
        }
        connection.setCatalog(config.getString("database.database"))
    }

    private fun createTable() {
        val sql = """
            CREATE TABLE IF NOT EXISTS player_particles (
                playerName VARCHAR(255) PRIMARY KEY,
                particles TEXT
            )
        """.trimIndent()
        connection.createStatement().use { statement ->
            statement.executeUpdate(sql)
        }
    }

    fun upsert(playerName: String, particles: List<String>) {
        val sql = "INSERT INTO player_particles (playerName, particles) VALUES (?, ?) ON DUPLICATE KEY UPDATE particles = VALUES(particles)"
        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, playerName)
            statement.setString(2, particles.joinToString(","))
            statement.executeUpdate()
        }
    }

    fun delete(playerName: String) {
        val sql = "DELETE FROM player_particles WHERE playerName = ?"
        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, playerName)
            statement.executeUpdate()
        }
    }

    fun select(playerName: String): List<String>? {
        val sql = "SELECT particles FROM player_particles WHERE playerName = ?"
        connection.prepareStatement(sql).use { statement ->
            statement.setString(1, playerName)
            val resultSet = statement.executeQuery()
            return if (resultSet.next()) {
                resultSet.getString("particles").split(",")
            } else {
                null
            }
        }
    }

    fun getAllPlayers(): List<String> {
        val sql = "SELECT playerName FROM player_particles"
        connection.createStatement().use { statement ->
            val resultSet = statement.executeQuery(sql)
            val players = mutableListOf<String>()
            while (resultSet.next()) {
                players.add(resultSet.getString("playerName"))
            }
            return players
        }
    }
}