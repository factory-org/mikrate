package factory.mikrate.dsl.matching

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import org.intellij.lang.annotations.Language

fun matchSqlString(@Language("GenericSQL") sqlString: String): Matcher<String> = SqlMatcher(sqlString)

private class SqlMatcher(private val sqlString: String) : Matcher<String> {
    override fun test(value: String): MatcherResult {
        val expected = transformSql(sqlString)
        val actual = transformSql(value)

        return MatcherResult(
            expected == actual,
            {
                """
                SQL statements should match:
                    expected:
                    ${sqlString.replace("\n", "\n    ")}
                    
                    actual:
                    ${value.replace("\n", "\n    ")}
                """.trimIndent()
            },
            {
                """
                SQL statements should not match:
                    expected:
                    ${sqlString.replace("\n", "\n    ")}
                    
                    actual:
                    ${value.replace("\n", "\n    ")}
                """.trimIndent()
            }
        )
    }

    private fun transformSql(sql: String): String {
        return sql.replace(whiteSpace, " ")
    }

    companion object {
        val whiteSpace by lazy { Regex("( \n)+") }
    }
}

infix fun String.shouldMatchSqlString(@Language("GenericSQL") sqlString: String) = this should matchSqlString(sqlString)
