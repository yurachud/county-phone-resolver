package resolver.country;

import com.google.common.base.CharMatcher;
import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.firstNonNull;

class Phone {
    private final String number;
    private final String rawNumber;
    private final int MAX_LENGTH = 17;
    private final int MIN_LENGTH = 4;
    private final ArrayList<CountryCode> countryCodes;
    
    public Phone(String rawNumber) {
        String onlyDigits = withoutCharacters(rawNumber);
        if (isEmpty(rawNumber)
                || tooLong(rawNumber)
                || tooShort(rawNumber)
                || not(onlyDigits)) {
            throw new InvalidPhoneNumber("Invalid phone format Length [4:17] allowed characters ['(' ')' '.' ' ' '_' '-']");
        }
        this.rawNumber = rawNumber;
        this.number = onlyDigits;
        this.countryCodes = new ArrayList<>();
        this.countryCodes.add(new CountryCode(number.substring(0, 4)));
        this.countryCodes.add(new CountryCode(number.substring(0, 3)));
        this.countryCodes.add(new CountryCode(number.substring(0, 2)));
        this.countryCodes.add(new CountryCode(number.substring(0, 1)));
    }
    
    public List<CountryCode> countryCodes() {
        return countryCodes;
    }
    
    public String number() {
        return number;
    }
    
    public String rawNumber() {
        return rawNumber;
    }
    
    private boolean not(String onlyDigits) {
        return CharMatcher.digit().matchesNoneOf(onlyDigits);
    }
    
    private boolean isEmpty(String rawNumber) {
        return Strings.isNullOrEmpty(rawNumber);
    }
    
    private boolean tooLong(String rawNumber) {
        return rawNumber.length() > MAX_LENGTH;
    }
    
    
    private boolean tooShort(String rawNumber) {
        return rawNumber.length() < MIN_LENGTH;
    }
    
    private String withoutCharacters(String rawNumber) {
        return firstNonNull(rawNumber, "")
                .replace("+", "")
                .replaceAll(" ", "")
                .replaceAll("\\.", "")
                .replaceAll("_", "")
                .replaceAll("\\(", "")
                .replaceAll("\\)", "")
                .replaceAll("-", "");
    }
    
    static class CountryCode {
        String value;
        
        public CountryCode(String value) {
            this.value = value;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CountryCode that = (CountryCode) o;
            return Objects.equal(value, that.value);
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }
        
    }
    
}
