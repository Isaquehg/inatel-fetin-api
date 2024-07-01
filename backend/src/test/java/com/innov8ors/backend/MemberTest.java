package com.innov8ors.backend;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.innov8ors.backend.model.Member;

import com.innov8ors.backend.model.Team;
import com.innov8ors.backend.model.User;

public class MemberTest {
    
     @Test
    public void testGettersAndSetters() {
    
        Member member = new Member();

        member.setId(1L);
        assertEquals(1L, member.getId());

        User user = new User();
        member.setUser(user);
        assertEquals(user, member.getUser());

        Team team = new Team();
        member.setTeam(team);
        assertEquals(team, member.getTeam());

        member.setResponsible_for_team(true);
        assertTrue(member.isResponsible_for_team());
    }
}
