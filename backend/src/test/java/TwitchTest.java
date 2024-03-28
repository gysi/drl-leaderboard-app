import de.gregord.drlleaderboardbackend.Main;
import de.gregord.drlleaderboardbackend.services.twitch.TwitchService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

//@SpringBootTest(
//        classes = Main.class
//)
//@ActiveProfiles({"test", "local-secrets"})
public class TwitchTest {

//    @Autowired
    private TwitchService twitchService;

//    @Test
    public void test() {
        System.out.println("Works");
        twitchService.getActiveStreams();
    }

}
