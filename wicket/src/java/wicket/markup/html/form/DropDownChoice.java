/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.markup.html.form;

import java.io.Serializable;
import java.util.Collection;

import wicket.RequestCycle;
import wicket.markup.ComponentTag;
import wicket.markup.html.form.model.IChoice;
import wicket.markup.html.form.model.IChoiceList;
import wicket.util.string.Strings;

/**
 * A choice implemented as a dropdown menu/list. Framework users can extend this
 * class and optionally implement interface
 * {@link wicket.markup.html.form.IOnChangeListener}to implement onChange
 * behaviour of the HTML select element.
 * 
 * @author Jonathan Locke
 * @author Eelco Hillenius
 * @author Johan Compagner
 */
public class DropDownChoice extends AbstractChoice implements IOnChangeListener
{
	/** serial UID. */
	private static final long serialVersionUID = 122777360064586107L;

	/**
	 * @see AbstractChoice#AbstractChoice(String, Collection)
	 */
	public DropDownChoice(String name, final Collection choices)
	{
		super(name, choices);
	}

	/**
	 * @see AbstractChoice#AbstractChoice(String, IChoiceList)
	 */
	public DropDownChoice(String name, final IChoiceList choices)
	{
		super(name, choices);
	}

	/**
	 * @see AbstractChoice#AbstractChoice(String, Serializable, Collection)
	 */
	public DropDownChoice(String name, Serializable object, final Collection choices)
	{
		super(name, object, choices);
	}
	
	/**
	 * @see AbstractChoice#AbstractChoice(String, Serializable, IChoiceList)
	 */
	public DropDownChoice(String name, Serializable object, final IChoiceList choices)
	{
		super(name, object, choices);
	}
	
	/**
	 * @see AbstractChoice#AbstractChoice(String, Serializable, String, Collection)
	 */
	public DropDownChoice(String name, Serializable object, String expression,
			final Collection choices)
	{
		super(name, object, expression, choices);
	}
	
	/**
	 * @see AbstractChoice#AbstractChoice(String, Serializable, String, IChoiceList)
	 */
	public DropDownChoice(String name, Serializable object, String expression,
			final IChoiceList choices)
	{
		super(name, object, expression, choices);
	}

	/**
	 * @see FormComponent#getValue()
	 */
	public final String getValue()
	{
		final IChoice choice = getChoices().choiceForObject(getModelObject());
		if (choice != null)
		{
			return choice.getId();
		}
		return "-1";
	}

	/**
	 * Called when a selection changes.
	 */
	public final void onSelectionChanged()
	{
		updateModel();
		onSelectionChanged(getModelObject());
	}

	/**
	 * @see FormComponent#setValue(java.lang.String)
	 */
	public final void setValue(final String value)
	{
		setModelObject(getChoices().choiceForId(value).getObject());
	}

	/**
	 * Processes the component tag.
	 * 
	 * @param tag
	 *            Tag to modify
	 * @see wicket.Component#onComponentTag(wicket.markup.ComponentTag)
	 */
	protected void onComponentTag(final ComponentTag tag)
	{
		// If a user subclasses this class and implements IOnChangeListener
		// an onChange scriptlet is added
		final String url = getRequestCycle().urlFor(this, IOnChangeListener.class);
		
		// Should the form be resubmitted if the selection changes?
		if (wantOnSelectionChangedNotifications())
		{
			// NOTE: do not encode the url as that would give invalid JavaScript
			tag.put("onChange", "location.href='" + url + "&" + getPath()
					+ "=' + this.options[this.selectedIndex].value;");
		}

		super.onComponentTag(tag);
	}

	/**
	 * Template method that can be overriden by clients that implement
	 * IOnChangeListener to be notified by onChange events of a select element.
	 * This method does nothing by default.
	 * <p>
	 * Called when a option is selected of a dropdown list that wants to be
	 * notified of this event. This method is to be implemented by clients that
	 * want to be notified of selection events.
	 * 
	 * @param newSelection
	 *            The newly selected object of the backing model NOTE this is
	 *            the same as you would get by calling getModelObject() if the
	 *            new selection were current
	 */
	protected void onSelectionChanged(final Object newSelection)
	{
	}

	/**
	 * Updates this components' model from the request.
	 * 
	 * @see wicket.markup.html.form.AbstractChoice#updateModel()
	 */
	protected final void updateModel()
	{
		final String id = getInput();
		if (Strings.isEmpty(id))
		{
			setModelObject(null);
		}
		else
		{
			setModelObject(getChoices().choiceForId(id).getObject());
		}
	}

	/**
	 * @return True if this component's onSelectionChanged event handler should
	 * 			called using javascript if the selection changes
	 */
	protected boolean wantOnSelectionChangedNotifications()
	{
		return false;
	}

	static
	{
		// Allow optional use of the IOnChangeListener interface
		RequestCycle.registerRequestListenerInterface(IOnChangeListener.class);
	}
}