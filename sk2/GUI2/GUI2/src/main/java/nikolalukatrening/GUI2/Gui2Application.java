package nikolalukatrening.GUI2;

import nikolalukatrening.GUI2.login.Login;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Gui2Application {

	public static void main(String[] args) {
		Login LoginFrame = new Login();
		LoginFrame.setVisible(true);
		LoginFrame.pack();
		LoginFrame.setLocationRelativeTo(null);
		SpringApplication.run(Gui2Application.class, args);
	}

}
