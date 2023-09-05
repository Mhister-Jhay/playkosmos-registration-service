package repository;

import connection.DbConnectionTest;
import model.Playmate;
import model.PlaymateDto;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlaymateRepositoryTest {


    @Test
    public void testRegisterPlaymate_Success_Email(){
        PlaymateRepository playmateRepository = new PlaymateRepository(DbConnectionTest.connection);
        Playmate playmate = new Playmate();
        playmate.setName("Test Playmate");
        playmate.setEmail("omosighojosh@gmail.com");
        playmate.setDateOfBirth("2001-01-01");
        playmate.setPassword("P@ssword1=");
        String result = playmateRepository.registerPlaymate(playmate);

        assertEquals("Playmate Registered Successfully", result);
    }

    @Test
    public void testRegisterPlaymate_Success_PhoneNumber(){
        PlaymateRepository playmateRepository = new PlaymateRepository(DbConnectionTest.connection);
        Playmate playmate = new Playmate();
        playmate.setName("Test Playmate");
        playmate.setPhoneNumber("+2348095727920");
        playmate.setDateOfBirth("2001-01-01");
        playmate.setPassword("P@ssword1=");
        String result = playmateRepository.registerPlaymate(playmate);

        assertEquals("Playmate Registered Successfully", result);
    }

    @Test
    public void testRegisterPlaymate_AlreadyExists_(){
        PlaymateRepository playmateRepository = new PlaymateRepository(DbConnectionTest.connection);
        Playmate playmate = new Playmate();
        playmate.setName("Test Playmate");
        playmate.setEmail("omosighojosh@gmail.com");
        playmate.setDateOfBirth("2001-01-01");
        playmate.setPassword("P@ssword1=");
        String result = playmateRepository.registerPlaymate(playmate);

        assertEquals("Playmate is already registered, Proceed to Login", result);
    }

    @Test
    public void testFetchPlaymateByEmail_Success(){
        PlaymateRepository playmateRepository = new PlaymateRepository(DbConnectionTest.connection);
        PlaymateDto playmateDto = playmateRepository.fetchPlaymate("omosighojosh@gmail.com", null);
        assertNotNull(playmateDto);
        assertEquals("Test Playmate", playmateDto.getName());
    }

    @Test
    public void testFetchPlaymateByPhoneNumber_Success(){
        PlaymateRepository playmateRepository = new PlaymateRepository(DbConnectionTest.connection);
        PlaymateDto playmateDto = playmateRepository.fetchPlaymate(null, "+2348095727920");

        assertNotNull(playmateDto);
        assertEquals("Test Playmate", playmateDto.getName());
    }

    @Test
    public void testFetchPlaymate_NotFound(){
        PlaymateRepository playmateRepository = new PlaymateRepository(DbConnectionTest.connection);
        PlaymateDto playmateDto = playmateRepository.fetchPlaymate("nonexistent@example.com", null);
        assertNull(playmateDto);
    }
}
