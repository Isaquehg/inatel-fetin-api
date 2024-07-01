package com.innov8ors.backend;
import static org.junit.jupiter.api.Assertions.*;
import com.innov8ors.backend.model.Team;
import com.innov8ors.backend.model.User;

import org.junit.jupiter.api.Test;


public class TeamTest {

    @Test
    public void testCreateTeamWithValidData() {
        String name = "Test Team";
        boolean isActive = true; // Using boolean for active

        Team team = new Team(name, isActive, new User());

        assertNotNull(team);
        assertEquals(name, team.getName());
        assertEquals(isActive, team.getActive());
    }

    @Test
    public void testActiveFlag() {
        Team team = new Team();

        // Test setting active to true
        team.setActive(true);
        assertTrue(team.getActive());

        // Test setting active to false
        team.setActive(false);
        assertFalse(team.getActive());
    }
}

