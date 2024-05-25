package honest.niceman.it.example.dto;

import java.time.Instant;
import java.util.Objects;

/**
 * DTO for {@link honest.niceman.it.example.model.User}
 */
public class UserDto {
    private final String username;
    private final Instant createdAt;
    private final String createdBy;

    public UserDto(String username,
                   Instant createdAt,
                   String createdBy) {
        this.username = username;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public String getUsername() {
        return username;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto entity = (UserDto) o;
        return Objects.equals(this.username, entity.username) &&
                Objects.equals(this.createdAt, entity.createdAt) &&
                Objects.equals(this.createdBy, entity.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, createdAt, createdBy);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "username = " + username + ", " +
                "createdAt = " + createdAt + ", " +
                "createdBy = " + createdBy + ")";
    }
}