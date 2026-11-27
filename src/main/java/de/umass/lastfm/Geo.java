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

import java.util.*;

/**
 * Provides nothing more than a namespace for the API methods starting with geo.
 *
 * @author Janni Kovacs
 */
public class Geo {

	private Geo() {
	}

	/**
	 * Get the most popular artists on Last.fm by country
	 *
	 * @param country A country name, as defined by the ISO 3166-1 country names standard
	 * @param apiKey A Last.fm API key.
	 * @return list of Artists
	 */
	public static Collection<Artist> getTopArtists(String country, String apiKey) {
		Result result = Caller.getInstance().call("geo.getTopArtists", apiKey, "country", country);
		return ResponseBuilder.buildCollection(result, Artist.class);
	}

	/**
	 * Get the most popular tracks on Last.fm by country
	 *
	 * @param country A country name, as defined by the ISO 3166-1 country names standard
	 * @param apiKey A Last.fm API key.
	 * @return a list of Tracks
	 */
	public static Collection<Track> getTopTracks(String country, String apiKey) {
		Result result = Caller.getInstance().call("geo.getTopTracks", apiKey, "country", country);
		return ResponseBuilder.buildCollection(result, Track.class);
	}
}
