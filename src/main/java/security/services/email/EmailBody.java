package security.services.email;

public class EmailBody {

    // The HTML body for the email.
    static final String HTMLBODY = "<h1>Thanks for Registering!</h1>"
            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
            + "Amazon SES</a> to welcome you to Heather Whyte's sample application.</a>";

    // The email body for recipients with non-HTML email clients.
    static final String TEXTBODY = "Thanks for Registering! \nThis email was sent through Amazon SES "
            + "to welcome you to Heather Whyte's sample application.";

    static final String HEADER = "Registration Complete";
}
