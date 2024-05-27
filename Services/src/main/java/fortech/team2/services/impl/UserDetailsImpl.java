package fortech.team2.services.impl;


import com.fasterxml.jackson.annotation.JsonIgnore;
import fortech.team2.model.Medic;
import fortech.team2.model.User;
import fortech.team2.model.enums.MedicSpecialization;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class UserDetailsImpl implements UserDetails {
    /**
     * You should extract this  constant in a separated class named 'Constants' in 'utils' package
     */
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String firstName;

    private String lastName;

    private String address;

    private String phone;


    private String email;

    @JsonIgnore
    private String password;

    private List<MedicSpecialization> specializations;

    private String education;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Integer id, String firstName, String lastName, String address, String phone, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        UserDetailsImpl userDetails = new UserDetailsImpl(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getAddress(),
                user.getPhone(),
                user.getEmail(),
                user.getPassword(),
                authorities);

        if (user instanceof Medic) {
            Medic medic = (Medic) user;
            userDetails.setEducation(medic.getEducation());
            userDetails.setSpecializations(medic.getSpecializations());
        }

        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Use getters and setters instead of writing manually
     */
    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<MedicSpecialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<MedicSpecialization> specializations) {
        this.specializations = specializations;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}