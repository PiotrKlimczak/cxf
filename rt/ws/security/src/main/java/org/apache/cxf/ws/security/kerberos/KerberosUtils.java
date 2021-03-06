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

package org.apache.cxf.ws.security.kerberos;

import javax.security.auth.callback.CallbackHandler;

import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.SecurityConstants;

/**
 * 
 */
public final class KerberosUtils {

    private KerberosUtils() {
        //utility class
    }
    
    public static KerberosClient getClient(Message message, String type) {
        KerberosClient client = (KerberosClient)message
            .getContextualProperty(SecurityConstants.KERBEROS_CLIENT);
        if (client == null) {
            client = new KerberosClient();
            
            String jaasContext = 
                (String)message.getContextualProperty(SecurityConstants.KERBEROS_JAAS_CONTEXT_NAME);
            String kerberosSpn = 
                (String)message.getContextualProperty(SecurityConstants.KERBEROS_SPN);
            CallbackHandler callbackHandler = 
                getCallbackHandler(
                    message.getContextualProperty(SecurityConstants.CALLBACK_HANDLER)
                );
            client.setContextName(jaasContext);
            client.setServiceName(kerberosSpn);
            client.setCallbackHandler(callbackHandler);
        }
        return client;
    }
    
    private static CallbackHandler getCallbackHandler(Object o) {
        CallbackHandler handler = null;
        if (o instanceof CallbackHandler) {
            handler = (CallbackHandler)o;
        } else if (o instanceof String) {
            try {
                handler = (CallbackHandler)ClassLoaderUtils.loadClass((String)o, 
                                                                      KerberosUtils.class).newInstance();
            } catch (Exception e) {
                handler = null;
            }
        }
        return handler;
    }
    
}
