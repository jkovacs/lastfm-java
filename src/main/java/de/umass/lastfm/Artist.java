/*
 * Copyright (c) 2012, the Last.fm Java Project and Committers
 * All rights reserved.
 *
 * Redistribution and use of this software in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.umass.lastfm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.umass.util.MapUtilities;
import de.umass.util.StringUtilities;
import de.umass.xml.DomElement;

/**
 * Bean that contains artist information.<br/> This class contains static methods that executes API methods relating to artists.<br/> Method
 * names are equivalent to the last.fm API method names.
 *
 * @author Janni Kovacs
 */
public class Artist extends MusicEntry {

	static final ItemFactory<Artist> FACTORY = new ArtistFactory();

	private Collection<Artist> similar = new ArrayList<Artist>();

	protected Artist(String name, String url) {
		super(name, url);
	}

	protected Artist(String name, String url, String mbid, int playcount, int listeners, boolean streamable) {
		super(name, url, mbid, playcount, listeners, streamable);
	}

	/**
	 * Returns a list of similar <code>Artist</code>s. Note that this method does not retrieve this list from the server but instead returns
	 * the result of an <code>artist.getInfo</code> call.<br/> If you need to retrieve similar artists to a specified artist use the {@link
	 * #getSimilar(String, String)} method.
	 *
	 * @return list of similar artists
	 * @see #getSimilar(String, String)
	 * @see #getSimilar(String, int, String)
	 */
	public Collection<Artist> getSimilar() {
		return similar;
	}

	/**
	 * Retrieves detailed artist info for the given artist or mbid entry.
	 *
	 * @param artistOrMbid Name of the artist or an mbid
	 * @param apiKey The API key
	 * @return detailed artist info
	 */
	public static Artist getInfo(String artistOrMbid, String apiKey) {
		return getInfo(artistOrMbid, null, null, apiKey);
	}

	/**
	 * Retrieves detailed artist info for the given artist or mbid entry.
	 *
	 * @param artistOrMbid Name of the artist or an mbid
	 * @param username The username for the context of the request, or <code>null</code>. If supplied, the user's playcount for this artist is
	 * included in the response
	 * @param apiKey The API key
	 * @return detailed artist info
	 */
	public static Artist getInfo(String artistOrMbid, String username, String apiKey) {
		return getInfo(artistOrMbid, null, username, apiKey);
	}

	/**
	 * Retrieves detailed artist info for the given artist or mbid entry.
	 *
	 * @param artistOrMbid Name of the artist or an mbid
	 * @param locale The language to fetch info in, or <code>null</code>
	 * @param username The username for the context of the request, or <code>null</code>. If supplied, the user's playcount for this artist is
	 * included in the response
	 * @param apiKey The API key
	 * @return detailed artist info
	 */
	public static Artist getInfo(String artistOrMbid, Locale locale, String username, String apiKey) {
		Map<String, String> params = new HashMap<String, String>();
		if (StringUtilities.isMbid(artistOrMbid)) {
			params.put("mbid", artistOrMbid);
		} else {
			params.put("artist", artistOrMbid);
		}
		if (locale != null && locale.getLanguage().length() != 0) {
			params.put("lang", locale.getLanguage());
		}
		MapUtilities.nullSafePut(params, "username", username);
		Result result = Caller.getInstance().call("artist.getInfo", apiKey, params);
		return ResponseBuilder.buildItem(result, Artist.class);
	}

	/**
	 * Calls {@link #getSimilar(String, int, String)} with the default limit of 100.
	 *
	 * @param artist Artist's name
	 * @param apiKey The API key
	 * @return similar artists
	 * @see #getSimilar(String, int, String)
	 */
	public static Collection<Artist> getSimilar(String artist, String apiKey) {
		return getSimilar(artist, 100, apiKey);
	}

	/**
	 * Returns <code>limit</code> similar artists to the given one.
	 *
	 * @param artist Artist's name
	 * @param limit Number of maximum results
	 * @param apiKey The API key
	 * @return similar artists
	 */
	public static Collection<Artist> getSimilar(String artist, int limit, String apiKey) {
		Result result = Caller.getInstance().call("artist.getSimilar", apiKey, "artist", artist, "limit", String.valueOf(limit));
		return ResponseBuilder.buildCollection(result, Artist.class);
	}

	/**
	 * Searches for an artist and returns a <code>Collection</code> of possible matches.
	 *
	 * @param name The artist name to look up
	 * @param apiKey The API key
	 * @return a list of possible matches
	 */
	public static Collection<Artist> search(String name, String apiKey) {
		Result result = Caller.getInstance().call("artist.search", apiKey, "artist", name);
		Collection<DomElement> children = result.getContentElement().getChild("artistmatches").getChildren("artist");
		List<Artist> list = new ArrayList<Artist>(children.size());
		for (DomElement c : children) {
			list.add(FACTORY.createItemFromElement(c));
		}
		return list;
	}

	/**
	 * Returns a list of the given artist's top albums.
	 *
	 * @param artist Artist's name
	 * @param apiKey The API key
	 * @return list of top albums
	 */
	public static Collection<Album> getTopAlbums(String artist, String apiKey) {
		Result result = Caller.getInstance().call("artist.getTopAlbums", apiKey, "artist", artist);
		return ResponseBuilder.buildCollection(result, Album.class);
	}

	/**
	 * Retrieves the top tags for the given artist.
	 *
	 * @param artist Artist's name
	 * @param apiKey The API key
	 * @return list of top tags
	 */
	public static Collection<Tag> getTopTags(String artist, String apiKey) {
		Result result = Caller.getInstance().call("artist.getTopTags", apiKey, "artist", artist);
		return ResponseBuilder.buildCollection(result, Tag.class);
	}

	/**
	 * Get the top tracks by an artist on Last.fm, ordered by popularity
	 *
	 * @param artist The artist name in question
	 * @param apiKey A Last.fm API key.
	 * @return list of top tracks
	 */
	public static Collection<Track> getTopTracks(String artist, String apiKey) {
		Result result = Caller.getInstance().call("artist.getTopTracks", apiKey, "artist", artist);
		return ResponseBuilder.buildCollection(result, Track.class);
	}

	/**
	 * Tag an artist with one or more user supplied tags.
	 *
	 * @param artist The artist name in question.
	 * @param tags A comma delimited list of user supplied tags to apply to this artist. Accepts a maximum of 10 tags.
	 * @param session A Session instance
	 * @return the result of the operation
	 */
	public static Result addTags(String artist, String tags, Session session) {
		return Caller.getInstance().call("artist.addTags", session, "artist", artist, "tags", tags);
	}

	/**
	 * Remove a user's tag from an artist.
	 *
	 * @param artist The artist name in question.
	 * @param tag A single user tag to remove from this artist.
	 * @param session A Session instance
	 * @return the result of the operation
	 */
	public static Result removeTag(String artist, String tag, Session session) {
		return Caller.getInstance().call("artist.removeTag", session, "artist", artist, "tag", tag);
	}

	/**
	 * Get the tags applied by an individual user to an artist on Last.fm.
	 *
	 * @param artist The artist name in question
	 * @param session A Session instance
	 * @return a list of tags
	 */
	public static Collection<String> getTags(String artist, Session session) {
		Result result = Caller.getInstance().call("artist.getTags", session, "artist", artist);
		if (!result.isSuccessful())
			return Collections.emptyList();
		DomElement element = result.getContentElement();
		Collection<String> tags = new ArrayList<String>();
		for (DomElement domElement : element.getChildren("tag")) {
			tags.add(domElement.getChildText("name"));
		}
		return tags;
	}

	/**
	 * Use the last.fm corrections data to check whether the supplied artist has a correction to a canonical artist. This method returns a new
	 * {@link Artist} object containing the corrected data, or <code>null</code> if the supplied Artist was not found.
	 *
	 * @param artist The artist name to correct
	 * @param apiKey A Last.fm API key
	 * @return a new {@link Artist}, or <code>null</code>
	 */
	public static Artist getCorrection(String artist, String apiKey) {
		Result result = Caller.getInstance().call("artist.getCorrection", apiKey, "artist", artist);
		if (!result.isSuccessful())
			return null;
		DomElement correctionElement = result.getContentElement().getChild("correction");
		if (correctionElement == null)
			return new Artist(artist, null);
		DomElement artistElem = correctionElement.getChild("artist");
		return FACTORY.createItemFromElement(artistElem);
	}

	private static class ArtistFactory implements ItemFactory<Artist> {
		public Artist createItemFromElement(DomElement element) {
			Artist artist = new Artist(null, null);
			MusicEntry.loadStandardInfo(artist, element);
			// similar artists
			DomElement similar = element.getChild("similar");
			if (similar != null) {
				Collection<DomElement> children = similar.getChildren("artist");
				for (DomElement child : children) {
					artist.similar.add(createItemFromElement(child));
				}
			}
			return artist;
		}
	}
}
