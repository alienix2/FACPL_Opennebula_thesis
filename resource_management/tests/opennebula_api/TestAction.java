package opennebula_api;

import java.util.List;

import opennebula_api.OpenNebulaActionBase;
import opennebula_api.OpenNebulaActionContext;

public class TestAction extends OpenNebulaActionBase {

    public TestAction(OpenNebulaActionContext ONActionContext) {
        super(ONActionContext);
    }

    @Override
    public void eval(List<Object> args) {}
}

