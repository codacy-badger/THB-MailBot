package cloud.bolte.thbmailbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

/*
 * ServerlistMOTD (c) by Strumswell, Philipp Bolte
 * ServerlistMOTD is licensed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * 
 * You should have received a copy of the license along with this work.
 * If not, see <http://creativecommons.org/licenses/by-nc-sa/3.0/>.
 * 
 * Used Librarys:
 *  - SimpleJavaMail http://www.simplejavamail.org/
 *    - JavaMail http://www.oracle.com/technetwork/java/javamail/index-138643.html
 *    - slf4j (api & simple.jar) https://www.slf4j.org/download.html
 *    - email-rfc2822-validator https://github.com/bbottema/email-rfc2822-validator
 *    
 */

public class MailBot {
	public static void main(String[] args) throws IOException {
        Email email = EmailBuilder.startingBlank()
                .from("MailBot", "#####@gmail.com")
                .to("Philipp", "#####@gmail.com")
                .withSubject("Ausfälle an der THB")
                .withPlainText(MailBot.getCancellations())
                .buildEmail();

        MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "username(without @gmail.com)", "password")
                .buildMailer()
                .sendMail(email);

        System.out.print("[MailBot] Job finished.");
	}
	
	/**
	 * Replacement pattern based on HTML code of the corresponding
	 * website. Pattern has to change if structure changes!
	 * 
	 * @return String of all cancellations 
	 * @throws IOException
	 */
	public static String getCancellations() throws IOException {
		URL thb = new URL("http://fbwcms.fh-brandenburg.de/abwesenheit/info.php");

		BufferedReader in = new BufferedReader(
		new InputStreamReader(thb.openStream()));

		String inputLine, result = "";
		while ((inputLine = in.readLine()) != null) {
			if (inputLine.contains("<h2 style=\"font-size:200%\";>")) {
                result += inputLine
                		.replace("<h2 style=\"font-size:200%\";>", "")
                		.replace("</h2>", "\n").replace("</body></html>", "");
             }
		}
		in.close();
		
		if (result.equalsIgnoreCase("")) {
            result = "Keine Ausfälle im System erfasst.\nLink: http://fbwcms.fh-brandenburg.de/abwesenheit/info.php";
		}
		return result;
	}
}
