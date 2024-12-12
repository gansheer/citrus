package org.citrusframework.camel.actions;

import org.citrusframework.context.TestContext;
import org.citrusframework.exceptions.CitrusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CamelPluginAction extends AbstractCamelJBangAction {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(CamelPluginAction.class);

    /** Camel Jbang plugin name */
    private final String pluginName;
    /** Camel Jbang plugin opearation */
    private final String pluginOperation;

    public CamelPluginAction(CamelPluginAction.Builder builder) {
        super("plugin", builder);
        this.pluginName = builder.pluginName;
        this.pluginOperation = builder.pluginOperation;
    }


    public String getPluginName() {
        return pluginName;
    }

    @Override
    public void doExecute(TestContext context) {
        logger.debug("Plugin action {}:{}", pluginOperation, pluginName);
        logger.info("Checking Camel plugins installed");
        List<String> installedPlugins = camelJBang().getPlugins();

        logger.info("Executing plugin operation");
        switch (pluginOperation){
            case "add":
                if(!installedPlugins.contains(pluginName)){
                    logger.debug("Installing plugin {}", pluginName);
                    List<String> fullArgs = List.of(pluginOperation, pluginName);
                    camelJBang().camelApp().run("plugin", fullArgs.toArray(String[]::new));
                } else {
                    logger.debug("Plugin {} already installed", pluginName);
                }
                break;
            case "delete":
                if(installedPlugins.contains(pluginName)){
                    logger.debug("Removing plugin {}", pluginName);
                    List<String> fullArgs = List.of(pluginOperation, pluginName);
                    camelJBang().camelApp().run("plugin", fullArgs.toArray(String[]::new));
                } else {
                    // add format
                    logger.debug("Plugin {} not installed", pluginName);
                }
                break;
            default:
                throw new CitrusRuntimeException("Invalid Camel plugin operation");
        }


    }

    /**
     * Action builder.
     */
    public static final class Builder extends AbstractCamelJBangAction.Builder<CamelPluginAction, CamelPluginAction.Builder> {
        private String pluginOperation;
        private String pluginName;

        /**
         * Set the plugin operation.
         * @param operation
         * @return
         */
        public CamelPluginAction.Builder pluginOperation(String operation) {
            this.pluginOperation = operation;
            return this;
        }

        /**
         * Sets the plugin name.
         * @param name
         * @return
         */
        public CamelPluginAction.Builder pluginName(String name) {
            this.pluginName = name;
            return this;
        }

        @Override
        public CamelPluginAction build() {
            return new CamelPluginAction(this);
        }
    }
}
