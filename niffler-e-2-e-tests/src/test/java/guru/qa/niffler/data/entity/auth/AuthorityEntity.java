package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.enums.Authority;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class AuthorityEntity implements Serializable {
    private UUID id;
    private Authority authority;
    private UserEntity user;
}
