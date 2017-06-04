package resolver.country;

import com.google.common.collect.ImmutableList;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class PhoneTest {
    @Test(expected = InvalidPhoneNumber.class)
    public void shouldThrowInvalidPhoneIfTooShort() throws Exception {
        new Phone("123");
    }
    
    @Test(expected = InvalidPhoneNumber.class)
    public void shouldThrowInvalidPhoneIfTooLong() throws Exception {
        new Phone("123456789123456789");
    }
    
    @Test(expected = InvalidPhoneNumber.class)
    public void shouldThrowInvalidPhoneIfIsEmpty() throws Exception {
        new Phone("");
    }
    
    @Test(expected = InvalidPhoneNumber.class)
    public void shouldThrowInvalidPhoneIfIsNull() throws Exception {
        new Phone(null);
    }
    
    @Test(expected = InvalidPhoneNumber.class)
    public void shouldThrowInvalidPhoneIfInvalidCharactersUsed() throws Exception {
        new Phone("asddd");
    }
    
    @Test
    public void shouldKeepRawNumber() throws Exception {
        assertThat(new Phone("+ 371 29875712").rawNumber(), Is.is("+ 371 29875712"));
        assertThat(new Phone("123.123.123").rawNumber(), Is.is("123.123.123"));
        assertThat(new Phone("()+_.- 123 123").rawNumber(), Is.is("()+_.- 123 123"));
    }
    
    @Test
    public void shouldKeepOnlyNumbersForNumber() throws Exception {
        assertThat(new Phone("+ 371 29875712").number(), Is.is("37129875712"));
        assertThat(new Phone("123.123.123").number(), Is.is("123123123"));
        assertThat(new Phone("()+_.- 123 123").number(), Is.is("123123"));
    }
    
    @Test
    public void shouldHaveCountryCodesFromLongestToShortest() throws Exception {
        assertThat(new Phone("+37129875712").countryCodes(), Is.is(
                ImmutableList.of(
                        new Phone.CountryCode("3712"),
                        new Phone.CountryCode("371"),
                        new Phone.CountryCode("37"),
                        new Phone.CountryCode("3"))));
    }
    
    @Test
    public void shouldImplementEqualsForCountryCode() throws Exception {
        assertThat(new Phone.CountryCode("1"), Is.is(new Phone.CountryCode("1")));
        assertThat(new Phone.CountryCode("2"), IsNot.not(new Phone.CountryCode("1")));
    }
}