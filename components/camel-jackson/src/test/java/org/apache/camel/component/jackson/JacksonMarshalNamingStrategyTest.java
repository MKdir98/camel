/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.jackson;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JacksonMarshalNamingStrategyTest extends CamelTestSupport {

    @Test
    public void testMarshalAndUnmarshalMap() throws Exception {
        PojoNamingStrategy pojoNamingStrategy = new PojoNamingStrategy();
        pojoNamingStrategy.setFieldOne("test");
        pojoNamingStrategy.setFieldTwo("supertest");

        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);
        Object marshalled = template.requestBody("direct:in", pojoNamingStrategy);
        String marshalledAsString = context.getTypeConverter().convertTo(String.class, marshalled);
        assertEquals("{\"field.one\":\"test\",\"field.two\":\"supertest\"}", marshalledAsString);

        mock.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {

            @Override
            public void configure() {
                JacksonDataFormat format = new JacksonDataFormat();
                format.setNamingStrategy("LOWER_DOT_CASE");
                from("direct:in").marshal(format).to("mock:result");
            }
        };
    }

}
