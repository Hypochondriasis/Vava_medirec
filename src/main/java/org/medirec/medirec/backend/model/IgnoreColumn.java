package org.medirec.medirec.backend.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

<<<<<<< Updated upstream
=======
/**
 * Annotation to mark fields that should be ignored during XML export or database operations
 */
>>>>>>> Stashed changes
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IgnoreColumn {}

