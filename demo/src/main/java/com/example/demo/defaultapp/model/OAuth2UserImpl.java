package com.example.demo.defaultapp.model;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import com.nimbusds.oauth2.sdk.util.MapUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

import static org.postgresql.shaded.com.ongres.scram.common.util.Preconditions.checkArgument;

public class OAuth2UserImpl implements OAuth2User, UserDetails {
    public static final String ID_ATTR = "id";

    private final String nameAttributeKey;

    private final Map<String, Object> attributes;

    private final Set<GrantedAuthority> authorities;

    public OAuth2UserImpl(String nameAttributeKey, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        checkArgument(MapUtils.isNotEmpty(attributes), "attributes cannot be empty");
        checkArgument(CollectionUtils.isNotEmpty(authorities), "authorities cannot be empty");
        checkArgument(StringUtils.isNotBlank(nameAttributeKey), "nameAttributeKey cannot be empty");

        validateAttributes(nameAttributeKey, attributes);

        this.nameAttributeKey = nameAttributeKey;
        this.attributes = new LinkedHashMap<>(attributes);
        this.authorities = new LinkedHashSet<>(sortAuthorities(authorities));
    }

    public Long getId() {
        return getAttribute(ID_ATTR);
    }

    @Override
    public String getName() {
        return getAttribute(nameAttributeKey);
    }

    @Override
    public String getUsername() {
        return getName();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        OAuth2UserImpl that = (OAuth2UserImpl) obj;

        if (!getId().equals(that.getId())) {
            return false;
        }
        if (!getName().equals(that.getName())) {
            return false;
        }
        if (!getAuthorities().equals(that.getAuthorities())) {
            return false;
        }
        return this.getAttributes().equals(that.getAttributes());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getAuthorities().hashCode();
        result = 31 * result + getAttributes().hashCode();
        return result;
    }

    @Override
    @SuppressWarnings("StringBufferReplaceableByString")
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: [");
        sb.append(getId());
        sb.append("], Name: [");
        sb.append(getName());
        sb.append("], Granted Authorities: [");
        sb.append(getAuthorities());
        sb.append("], User Attributes: [");
        sb.append(getAttributes());
        sb.append("]");
        return sb.toString();
    }

    private void validateAttribute(String attrName, Map<String, Object> attributes) {
        if (!attributes.containsKey(attrName)) {
            throw new IllegalArgumentException("Missing " + attrName + " attribute in attributes");
        }
    }

    private void validateAttributes(String nameAttributeKey, Map<String, Object> attributes) {
        validateAttribute(ID_ATTR, attributes);
        validateAttribute(nameAttributeKey, attributes);
    }

    private Set<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(Comparator.comparing(GrantedAuthority::getAuthority));
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
    }
}
