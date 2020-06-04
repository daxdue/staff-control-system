package ru.indoornav.authentificate;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import ru.indoornav.lambdaworks.crypto.SCryptUtil;
import spark.FilterImpl;
import spark.Request;
import spark.Response;
import spark.utils.SparkUtils;

import static spark.Spark.halt;

public class BasicAuthenticationFilter extends FilterImpl
{
    private static final String BASIC_AUTHENTICATION_TYPE = "Basic";

    private static final int NUMBER_OF_AUTHENTICATION_FIELDS = 2;

    private static final String ACCEPT_ALL_TYPES = "*";


    private final AuthenticationDetails authenticationDetails;


    public BasicAuthenticationFilter(final AuthenticationDetails authenticationDetails)
    {
        this(SparkUtils.ALL_PATHS, authenticationDetails);
    }

    public BasicAuthenticationFilter(final String path, final AuthenticationDetails authenticationDetails)
    {
        super(path, ACCEPT_ALL_TYPES);
        this.authenticationDetails = authenticationDetails;
    }

    @Override
    public void handle(final Request request, final Response response)
    {
        final String encodedHeader = StringUtils.substringAfter(request.headers("Authorization"), "Basic");

        if (notAuthenticatedWith(credentialsFrom(encodedHeader)))
        {
            response.header("WWW-Authenticate", BASIC_AUTHENTICATION_TYPE);
            halt(401);
        }
    }

    private String[] credentialsFrom(final String encodedHeader)
    {
        return StringUtils.split(encodedHeader != null ? decodeHeader(encodedHeader) : null, ":");

    }

    private String decodeHeader(final String encodedHeader)
    {

        return new String(Base64.decodeBase64(encodedHeader.getBytes()));
    }

    private boolean notAuthenticatedWith(final String[] credentials)
    {
        return !authenticatedWith(credentials);
    }

    private boolean authenticatedWith(final String[] credentials)
    {
        if (credentials != null && credentials.length == NUMBER_OF_AUTHENTICATION_FIELDS)
        {
            final String submittedUsername = credentials[0];
            final String submittedPassword = credentials[1];



            if(authenticationDetails.isSuperuser) {
                PBKDF2Hasher hasher = new PBKDF2Hasher();
                authenticationDetails.isSuperuser = false;
                return StringUtils.equals(submittedUsername, authenticationDetails.username) && SCryptUtil.check(submittedPassword, authenticationDetails.getToken());

            } else {
                return authenticationDetails.authentificate(submittedUsername, submittedPassword);
            }
        }
        else
        {
            return false;
        }
    }
}