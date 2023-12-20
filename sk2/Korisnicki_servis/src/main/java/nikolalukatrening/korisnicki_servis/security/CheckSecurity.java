package nikolalukatrening.korisnicki_servis.security;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // sluzi da se anotacija primeni na metodu
@Retention(RetentionPolicy.RUNTIME) // sluzi da se anotacija zadrzi u runtime-u
public @interface CheckSecurity {

    String[] roles() default {}; // default vrednost je prazan niz
}
