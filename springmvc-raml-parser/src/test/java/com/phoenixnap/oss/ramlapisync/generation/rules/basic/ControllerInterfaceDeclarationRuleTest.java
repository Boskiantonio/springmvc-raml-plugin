package com.phoenixnap.oss.ramlapisync.generation.rules.basic;

import com.phoenixnap.oss.ramlapisync.generation.rules.AbstractControllerRuleTestBase;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JPackage;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author armin.weisser
 * @since 0.4.1
 */
public class ControllerInterfaceDeclarationRuleTest extends AbstractControllerRuleTestBase {

    private ControllerInterfaceDeclarationRule rule = new ControllerInterfaceDeclarationRule();

    @Test
    public void applyRule_shouldCreate_validControllerInterface() {
        JPackage jPackage = jCodeModel.rootPackage();
        JClass jClass = rule.apply(getControllerMetadata(), jPackage);
        assertThat(jClass, is(notNullValue()));
        assertThat(jClass.name(), equalTo("BaseController"));
        assertThat(serializeModel(), containsString("interface BaseController"));
    }

    @Test
    public void applyClassRule_shouldBeIdempotent() {
        JPackage jPackage = jCodeModel.rootPackage();

        JClass jClass1 = rule.apply(getControllerMetadata(), jPackage);
        String serialized1 = serializeModel();

        JClass jClass2 = rule.apply(getControllerMetadata(), jPackage);
        String serialized2 = serializeModel();

        assertThat(jClass1, equalTo(jClass2));
        assertEquals(serialized1, serialized2);
    }

}
