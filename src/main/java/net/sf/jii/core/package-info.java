/**
 * Contain the wrapper objects (core interfaces) for the different implementations of {@link net.sf.jii.IInfoProvider}. 
 * It is strongly recommended to use only these wrapper instead of the concrete implemented objects because they 
 * have a clearly interface structure.<br/>
 * <br/>
 * <strong>Tip: </strong> If you want to use the wrappers in a threaded environment, you *must* use one instance per thread.
 * In other words: The wrappers don't support synchronization!
 */
package net.sf.jii.core;