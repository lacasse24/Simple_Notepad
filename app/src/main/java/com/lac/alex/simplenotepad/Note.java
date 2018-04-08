package com.lac.alex.simplenotepad;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Alex on 2017-06-19.
 */

public class Note extends Object
{
    private UUID m_Id; // Universal Unique Identifier
    private String m_Title;
    private String m_Content;
    private Date m_Date;
    private String m_ImgPath;


    public Note()
    {
        m_Id = UUID.randomUUID(); // Generate a unique identifier
        m_Date = new Date(); // Sets the m_Date object to the current date
    }

    //To recover data from the serialized JSON object
    public Note (JSONObject json) throws JSONException
    {
        m_Id = UUID.fromString(json.getString("id"));
        if(json.has("title"))
        {
            m_Title = json.getString("title");
        }
        m_Content = json.getString("content");
        m_Date = new Date(json.getLong("date"));
        m_ImgPath = json.getString("imgpath");
    }

    // Handle the business of converting the data of a Crime into something that can be written to a file as JSON.
    public JSONObject toJSON() throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("id",m_Id.toString());
        json.put("title",m_Title);
        json.put("content",m_Content);
        json.put("date",m_Date.getTime());
        json.put("imgpath",m_ImgPath);

        return json;
    }

    public UUID getId()
    {
        return m_Id;
    }

    public String getTitle()
    {
        return m_Title;
    }
    public void setTitle(String Title)
    {
        m_Title = Title;
    }

    public String getContent()
    {
        return m_Content;
    }
    public void setContent(String Content)
    {
        m_Content = Content;
    }

    public Date getDate(){return m_Date;}
    public void setDate (Date date){m_Date=date;}

    public String getImgPath() {return m_ImgPath;}
    public void setImgPath(String m_ImgPath) {this.m_ImgPath = m_ImgPath;}



}
