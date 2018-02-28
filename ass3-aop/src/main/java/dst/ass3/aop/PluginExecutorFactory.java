package dst.ass3.aop;

import dst.ass3.aop.impl.PluginExecutor;

import java.io.IOException;

public class PluginExecutorFactory {

	public static IPluginExecutor createPluginExecutor() {
		try {
			return new PluginExecutor();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
