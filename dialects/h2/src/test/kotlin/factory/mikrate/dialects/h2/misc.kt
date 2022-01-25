package factory.mikrate.dialects.h2

import java.sql.Connection
import java.sql.DriverManager

fun h2Connection(): Connection = DriverManager.getConnection("jdbc:h2:mem:test")
