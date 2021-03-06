/*******************************************************************************
 * 	Copyright 2016 ContainX and OpenStack4j                                          
 * 	                                                                                 
 * 	Licensed under the Apache License, Version 2.0 (the "License"); you may not      
 * 	use this file except in compliance with the License. You may obtain a copy of    
 * 	the License at                                                                   
 * 	                                                                                 
 * 	    http://www.apache.org/licenses/LICENSE-2.0                                   
 * 	                                                                                 
 * 	Unless required by applicable law or agreed to in writing, software              
 * 	distributed under the License is distributed on an "AS IS" BASIS, WITHOUT        
 * 	WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the         
 * 	License for the specific language governing permissions and limitations under    
 * 	the License.                                                                     
 *******************************************************************************/
package com.huawei.openstack4j.api.workflow;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.huawei.openstack4j.api.workflow.ActionDefinitionService;
import com.huawei.openstack4j.model.common.ActionResponse;
import com.huawei.openstack4j.model.workflow.ActionDefinition;
import com.huawei.openstack4j.model.workflow.Scope;

import java.util.List;

import static org.testng.Assert.*;

/**
 * Test cases for {@link ActionDefinitionService}.
 * 
 * @author Renat Akhmerov
 */
@Test(suiteName="ActionDefinitions")
public class ActionDefinitionTest extends WorkflowBaseTest {

    private static final String JSON_ACTION_DEF = "/workflow/action_def.json";
    private static final String JSON_ACTION_DEFS = "/workflow/action_defs.json";
    private static final String NEW_ACTION = "/workflow/new_action.yaml";
    private static final String JSON_ACTION_DEF_CREATE = "/workflow/action_def_create.json";

    private ActionDefinitionService service;

    @BeforeTest
    public void setUp() {
        this.service = osv3().workflow().actionDefinitions();
    }

    @Test
    public void listActionDefinitions() throws Exception {
        respondWith(JSON_ACTION_DEFS);
        
        List<? extends ActionDefinition> actionDefs = service.list();

        assertEquals(actionDefs.size(), 2);

        // Check first action definition.
        ActionDefinition actionDef = actionDefs.get(0);

        assertNotNull(actionDef);
        assertIsUUID(actionDef.getId());
        assertNotEmptyString(actionDef.getProjectId());
        assertEquals(actionDef.getName(), "concat");
        assertNotNull(actionDef.getTags());
        assertEquals(actionDef.getTags().get(0), "test");
        assertEquals(actionDef.getTags().get(1), "custom");
        assertNotNull(actionDef.getCreatedAt());
        assertNull(actionDef.getUpdatedAt());
        assertNotEmptyString(actionDef.getDefinition());
        assertTrue(actionDef.getDefinition().contains("base: std.echo"));
        assertEquals(actionDef.getInput(), "s1, s2");

        // Check second action definition.
        actionDef = actionDefs.get(1);

        assertNotNull(actionDef);
        assertIsUUID(actionDef.getId());
        assertNotEmptyString(actionDef.getProjectId());
        assertEquals(actionDef.getName(), "concat_twice");
        assertNotNull(actionDef.getTags());
        assertEquals(actionDef.getTags().size(), 0);
        assertNotNull(actionDef.getCreatedAt());
        assertNull(actionDef.getUpdatedAt());
        assertNotEmptyString(actionDef.getDefinition());
        assertTrue(actionDef.getDefinition().contains("<% $ %> and <% $ %>"));
        assertEquals(actionDef.getInput(), "s1, s2");
    }

    @Test
    public void getActionDefinition() throws Exception {
        respondWith(JSON_ACTION_DEF);

        ActionDefinition actionDef = service.get("eecf6cad-65af-4a11-9e6f-692b23ffac08");

        assertNotNull(actionDef);
        assertIsUUID(actionDef.getId());
        assertNotEmptyString(actionDef.getProjectId());
        assertEquals(actionDef.getName(), "concat");
        assertNotNull(actionDef.getTags());
        assertEquals(actionDef.getTags().get(0), "test");
        assertEquals(actionDef.getTags().get(1), "custom");
        assertNotNull(actionDef.getCreatedAt());
        assertNull(actionDef.getUpdatedAt());
        assertNotEmptyString(actionDef.getDefinition());
        assertTrue(actionDef.getDefinition().contains("base: std.echo"));
        assertEquals(actionDef.getInput(), "s1, s2");
    }

    @Test
    public void createActionDefinition() throws Exception {
        respondWith(JSON_ACTION_DEF_CREATE);

        List<? extends ActionDefinition> actionDefs = service.create(
                getClass().getResourceAsStream(NEW_ACTION),
                Scope.PRIVATE
        );

        assertEquals(actionDefs.size(), 1);

        ActionDefinition actionDef = actionDefs.get(0);

        assertNotNull(actionDef);
        assertIsUUID(actionDef.getId());
        assertNotEmptyString(actionDef.getProjectId());
        assertEquals(actionDef.getName(), "concat");
        assertNotNull(actionDef.getTags());
        assertEquals(actionDef.getTags().get(0), "test");
        assertEquals(actionDef.getTags().get(1), "custom");
        assertNotNull(actionDef.getCreatedAt());
        assertNull(actionDef.getUpdatedAt());
        assertNotEmptyString(actionDef.getDefinition());
        assertTrue(actionDef.getDefinition().contains("base: std.echo"));
        assertEquals(actionDef.getInput(), "s1, s2");
    }

    @Test
    public void deleteActionDefinition() throws Exception {
        respondWith(204); // No content.

        ActionResponse resp = service.delete("concat");

        // TODO(rakhmerov): Change to 204 once ActionResponse can return other 2xx codes.
        assertEquals(resp.getCode(), 200);
    }
}
