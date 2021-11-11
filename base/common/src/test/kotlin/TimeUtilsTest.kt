import com.google.common.truth.Truth
import com.harera.time.TimeUtils
import org.joda.time.DateTime
import org.junit.Test

class TimeUtilsTest {

    @Test
    fun `3 h must equals 10800`() {
        TimeUtils.timeFromNow(DateTime.now().minus(10800)).let {
            Truth.assertThat(it).isEqualTo("3 h")
        }
    }
}