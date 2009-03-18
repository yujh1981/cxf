/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.cxf.jaxrs.client;

import java.net.URI;

import org.junit.Assert;
import org.junit.Test;


public class WebClientTest extends Assert {

    @Test 
    public void testBaseCurrentPath() {
        assertEquals(URI.create("http://foo"), new WebClient("http://foo").getBaseURI());
        assertEquals(URI.create("http://foo"), new WebClient("http://foo").getCurrentURI());
    }
    
    @Test 
    public void testNewBaseCurrentPath() {
        WebClient wc = new WebClient("http://foo");
        assertEquals(URI.create("http://foo"), wc.getBaseURI());
        assertEquals(URI.create("http://foo"), wc.getCurrentURI());
        wc.to("http://bar", false);
        assertEquals(URI.create("http://bar"), wc.getBaseURI());
        assertEquals(URI.create("http://bar"), wc.getCurrentURI());
    }
    
    @Test 
    public void testForward() {
        WebClient wc = new WebClient("http://foo");
        wc.to("http://foo/bar", true);
        assertEquals(URI.create("http://foo"), wc.getBaseURI());
        assertEquals(URI.create("http://foo/bar"), wc.getCurrentURI());
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testWrongForward() {
        WebClient wc = new WebClient("http://foo");
        wc.to("http://bar", true);
    }
    
    @Test 
    public void testBaseCurrentPathAfterChange() {
        WebClient wc = new WebClient(URI.create("http://foo"));
        wc.path("bar").path("baz").matrix("m1", "m1value").query("q1", "q1value");
        assertEquals(URI.create("http://foo"), wc.getBaseURI());
        assertEquals(URI.create("http://foo/bar/baz;m1=m1value?q1=q1value"), wc.getCurrentURI());
    }
    
    
    @Test 
    public void testBaseCurrentPathAfterCopy() {
        WebClient wc = new WebClient(URI.create("http://foo"));
        wc.path("bar").path("baz").matrix("m1", "m1value").query("q1", "q1value");
        WebClient wc1 = new WebClient(wc);
        assertEquals(URI.create("http://foo/bar/baz;m1=m1value?q1=q1value"), wc1.getBaseURI());
        assertEquals(URI.create("http://foo/bar/baz;m1=m1value?q1=q1value"), wc1.getCurrentURI());
    }
    
    @Test 
    public void testHeaders() {
        WebClient wc = new WebClient(URI.create("http://foo"));
        wc.header("h1", "h1value").header("h2", "h2value");
        assertEquals(1, wc.getHeaders().get("h1").size());
        assertEquals("h1value", wc.getHeaders().getFirst("h1"));
        assertEquals(1, wc.getHeaders().get("h2").size());
        assertEquals("h2value", wc.getHeaders().getFirst("h2"));
        wc.getHeaders().clear();
        assertEquals(1, wc.getHeaders().get("h1").size());
        assertEquals("h1value", wc.getHeaders().getFirst("h1"));
        assertEquals(1, wc.getHeaders().get("h2").size());
        assertEquals("h2value", wc.getHeaders().getFirst("h2"));
        wc.reset();
        assertTrue(wc.getHeaders().isEmpty());
    }
    
    @Test
    public void testBackFast() {
        WebClient wc = new WebClient(URI.create("http://foo"));
        wc.path("bar").path("baz").matrix("m1", "m1value");
        assertEquals(URI.create("http://foo"), wc.getBaseURI());
        assertEquals(URI.create("http://foo/bar/baz;m1=m1value"), wc.getCurrentURI());
        wc.back(true);
        assertEquals(URI.create("http://foo"), wc.getCurrentURI());
    }
    
    @Test
    public void testBack() {
        WebClient wc = new WebClient(URI.create("http://foo"));
        wc.path("bar").path("baz");
        assertEquals(URI.create("http://foo"), wc.getBaseURI());
        assertEquals(URI.create("http://foo/bar/baz"), wc.getCurrentURI());
        wc.back(false);
        assertEquals(URI.create("http://foo/bar"), wc.getCurrentURI());
        wc.back(false);
        assertEquals(URI.create("http://foo"), wc.getCurrentURI());
        wc.back(false);
        assertEquals(URI.create("http://foo"), wc.getCurrentURI());
    }
    
}
