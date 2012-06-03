package com.github.danfickle.javasqlquerybuilder.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.danfickle.javasqlquerybuilder.QbField;
import com.github.danfickle.javasqlquerybuilder.QbInsert;

/**
 * The default implementation of QbInsert.
 * @author DanFickle
 */
public class QbInsertImp implements QbInsert 
{
	private QbInsertImp() { }
	private String m_table;
	private Map<String, Integer> m_placeholders;
	private List<QbField> m_fields;

	@Override
	public String getQueryString()
	{
		if (m_fields == null || m_table == null || m_placeholders == null)
			throw new IllegalStateException("Table name or fields missing");
		
		StringBuilder builder = new StringBuilder("INSERT INTO ");
		builder.append(QbCommonImp.protectTableName(m_table));
		builder.append('(');
		
		for (QbField field : m_fields)
		{
			builder.append(field.toString());
			builder.append(',');
			builder.append(' ');
		}

		builder.append(") VALUES (");
		
		for (int i = 0; i < m_fields.size(); i++)
		{
			builder.append('?');
			builder.append(',');
			builder.append(' ');
		}

		builder.append(')');
		
		return builder.toString();
	}

	@Override
	public int getPlaceholderIndex(String placeholderName)
	{
		if (m_placeholders == null)
			throw new IllegalArgumentException("Placeholder doesn't exist");
		
		Integer idx = m_placeholders.get(placeholderName);
		
		if (idx == null)
			throw new IllegalArgumentException("Placeholder doesn't exist");
		else
			return idx;
	}

	@Override
	public QbInsert set(QbField field, String placeholder)
	{
		if (m_fields == null)
			m_fields = new ArrayList<QbField>();
		if (m_placeholders == null)
			m_placeholders = new HashMap<String, Integer>();
		
		if (m_placeholders.containsKey(placeholder))
			throw new IllegalArgumentException("Duplicate placeholder");
		
		m_fields.add(field);
		m_placeholders.put(placeholder, m_placeholders.size());
		return this;
	}

	@Override
	public QbInsert inTable(String table)
	{
		m_table = table;
		return this;
	}
}