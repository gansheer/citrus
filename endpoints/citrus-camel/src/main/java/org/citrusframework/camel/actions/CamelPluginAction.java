package org.citrusframework.camel.actions;

import org.citrusframework.context.TestContext;
import org.citrusframework.exceptions.CitrusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class CamelPluginAction extends AbstractCamelJBangAction {

    /** Logger */
    private static final Logger logger = LoggerFactory.getLogger(CamelPluginAction.class);

    /** Camel Jbang plugin name */
    private final String pluginName;
    /** Camel Jbang plugin opearation */
    private final String pluginOperation;
    /** Camel Jbang command arguments */
    private final List<String> args;

    public CamelPluginAction(CamelPluginAction.Builder builder) {
        super("plugin", builder);
        this.pluginName = builder.pluginName;
        this.pluginOperation = builder.pluginOperation;
        this.args = builder.args;
    }


    public String getPluginName() {
        return pluginName;
    }
    public String getPluginOperation() {
        return pluginOperation;
    }

    public List<String> getArgs() {
        return args;
    }

    @Override
    public void doExecute(TestContext context) {
        logger.debug("Plugin action {}:{}", pluginOperation, pluginName);

        logger.info("Checking Camel plugins installed");
        Map<String, String> installedPlugins = camelJBang().getPlugins();

        logger.info("Executing plugin operation");
        switch (pluginOperation){
            case "add":
                logger.debug("Install plugin {}", pluginName);
                if(!installedPlugins.containsKey(pluginName)){
                    List<String> fullArgs = List.of(pluginOperation, pluginName);
                    fullArgs.addAll(args);
                    camelJBang().camelApp().run("plugin", fullArgs.toArray(String[]::new));
                } else {
                    logger.debug("Plugin {} already installed", pluginName);
                }
                break;
            case "command":
                logger.debug("Execute plugin command {}", pluginName);
                if(installedPlugins.containsValue(pluginName)) {
                    List<String> fullArgs = List.copyOf(args);
                    camelJBang().camelApp().run(pluginName, fullArgs.toArray(String[]::new));
                } else{
                    throw new CitrusRuntimeException(format("Camel plugin command '%s' not available", pluginName));
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
        private final List<String> args = new ArrayList<>();

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


        /**
         * Adds a command argument.
         * @param arg
         * @return
         */
        public CamelPluginAction.Builder withArg(String arg) {
            this.args.add(arg);
            return this;
        }

        /**
         * Adds a command argument with name and value.
         * @param name
         * @param value
         * @return
         */
        public CamelPluginAction.Builder withArg(String name, String value) {
            this.args.add(name);
            this.args.add(value);
            return this;
        }

        /**
         * Adds command arguments.
         * @param args
         * @return
         */
        public CamelPluginAction.Builder withArgs(String... args) {
            this.args.addAll(Arrays.asList(args));
            return this;
        }

        @Override
        public CamelPluginAction build() {
            return new CamelPluginAction(this);
        }
    }
}
