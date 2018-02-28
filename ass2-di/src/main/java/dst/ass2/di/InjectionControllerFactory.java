package dst.ass2.di;

import dst.ass2.di.impl.InjectionControler;

/**
 * Creates and provides {@link IInjectionController} instances.
 */
public class InjectionControllerFactory {
    private  static IInjectionController injectionController = new InjectionControler();


	/**
	 * Returns the singleton {@link IInjectionController} instance.<br/>
	 * If none is available, a new one is created.
	 *
	 * @return the instance
	 */
	public static synchronized IInjectionController getStandAloneInstance() {
		return injectionController;
	}

	/**
	 * Returns the singleton {@link IInjectionController} instance for processing objects modified by an
	 * {@link dst.ass2.di.agent.InjectorAgent InjectorAgent}.<br/>
	 * If none is available, a new one is created.
	 *
	 * @return the instance
	 */

	public static synchronized IInjectionController getTransparentInstance() {

        return null;
	}

	/**
	 * Creates and returns a new {@link IInjectionController} instance.
	 *
	 * @return the newly created instance
	 */
	public static IInjectionController getNewStandaloneInstance() {
		return new InjectionControler();
	}

	/**
	 * Creates and returns a new {@link IInjectionController} instance for processing objects modified by an
	 * {@link dst.ass2.di.agent.InjectorAgent InjectorAgent}.<br/>
	 *
	 * @return the instance
	 */
	public static IInjectionController getNewTransparentInstance() {

        return null;

    }
}
