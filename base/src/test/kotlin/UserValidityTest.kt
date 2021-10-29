import com.google.common.truth.Truth
import com.harera.base.utils.Validity
import org.junit.Test
import org.koin.test.KoinTest

class UserValidityTest : KoinTest {

    @Test
    fun `empty email should return false`() {
        Validity.checkEmail("").also {
            Truth.assertThat(it).isFalse()
        }
    }

    @Test
    fun `email doesn't contains @ should return false`() {
        Validity.checkEmail("Hasanan.Hasna.com").also {
            Truth.assertThat(it).isFalse()
        }
    }

    @Test
    fun `full email should return true`() {
        Validity.checkEmail("Hassan.Hassan.Hassan@gmail.com").also {
            Truth.assertThat(it).isTrue()
        }
    }
}