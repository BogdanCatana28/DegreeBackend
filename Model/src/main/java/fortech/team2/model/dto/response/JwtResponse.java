package fortech.team2.model.dto.response;

import java.util.List;

public class JwtResponse {
    private String token;
    /** If its value cannot be changed once it has been initialized, you should add the 'final' keyword  **/
    private String type = "Bearer";
    private String refreshToken;
    private Integer id;
    private String email;
    private List<String> roles;

    /** Instead of manually write constructors, getters and setters for all variables, use @Getter, @Setter, @AllArgsContructor annotations
     */
    public JwtResponse(String accessToken, String refreshToken, Integer id, String email, List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}