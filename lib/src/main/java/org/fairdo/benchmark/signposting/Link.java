
/*
 * Copyright 2012-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Modifications by Stian Soiland-Reyes:
 * - Source https://github.com/spring-projects/spring-hateoas/blob/2.4.1/src/main/java/org/springframework/hateoas/Link.java
 * - Moved from spring-hateaos to fdobenchmark, changing Java package
 * - Removed Spring and JSON dependencies
 * - Removed affordances, deprecation, template
 * - Null assertions replaced with Objects.requireNonNull
 * - Reformatting
 */

package org.fairdo.benchmark.signposting;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import org.fairdo.benchmark.signposting.LinkRelation.IanaLinkRelations;

/**
 * Value object for links.
 *
 * @author Oliver Gierke
 * @author Greg Turnquist
 * @author Jens Schauder
 * @author Viliam Durina
 * @author Stian Soiland-Reyes
 */
public class Link {

	private String href;
	private LinkRelation rel;
	// Optionals
	private String hreflang, media, title, type, profile, name;

	/**
	 * Creates a new {@link Link} to the given URI with the given rel.
	 *
	 * @param href must not be {@literal null} or empty.
	 * @param rel  must not be {@literal null} or empty.
	 */
	protected Link(String href, LinkRelation rel) {
		this.href = Objects.requireNonNull(href);
		this.rel = Objects.requireNonNull(rel);
	}

	Link(LinkRelation rel, String href, String hreflang, String media, String title,
			 String type, String profile, String name) {

		this.rel = Objects.requireNonNull(rel);
		this.href = Objects.requireNonNull(href);
		this.hreflang = hreflang;
		this.media = media;
		this.title = title;
		this.type = type;
		this.profile = profile;
		this.name = name;
	}

	/**
	 * Creates a new link to the given URI with the self relation.
	 *
	 * @see IanaLinkRelations#SELF
	 * @param href must not be {@literal null} or empty.
	 * @return
	 * @since 1.1
	 */
	public static Link of(String href) {
		return new Link(href, IanaLinkRelations.SELF);
	}

	/**
	 * Creates a new {@link Link} to the given href with the given relation.
	 *
	 * @param href     must not be {@literal null} or empty.
	 * @param relation must not be {@literal null} or empty.
	 * @return
	 * @since 1.1
	 */
	public static Link of(String href, String relation) {
		return new Link(href, LinkRelation.of(relation));
	}

	/**
	 * Creates a new {@link Link} to the given href and {@link LinkRelation}.
	 *
	 * @param href     must not be {@literal null} or empty.
	 * @param relation must not be {@literal null}.
	 * @return
	 * @since 1.1
	 */
	public static Link of(String href, LinkRelation relation) {
		return new Link(href, relation);
	}

	/**
	 * Empty constructor required by the marshaling framework.
	 */
	protected Link() {
	}

	/**
	 * Returns a {@link Link} pointing to the same URI but with the {@code self}
	 * relation.
	 *
	 * @return
	 */
	public Link withSelfRel() {
		return withRel(IanaLinkRelations.SELF);
	}

	/**
	 * Creates a new {@link Link} with the same href but given {@link LinkRelation}.
	 *
	 * @param relation must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public Link withRel(LinkRelation relation) {
		return new Link(Objects.requireNonNull(relation), href, hreflang, media, title, type, profile, name);
	}

	/**
	 * Creates a new {@link Link} with the same href but given {@link LinkRelation}.
	 *
	 * @param relation must not be {@literal null} or empty.
	 * @return will never be {@literal null}.
	 */
	public Link withRel(String relation) {
		return withRel(LinkRelation.of(relation));
	}

	/**
	 * Returns whether the current {@link Link} has the given link relation.
	 *
	 * @param rel must not be {@literal null} or empty.
	 * @return
	 */
	public boolean hasRel(String rel) {
		assert rel != null && !rel.isEmpty();
		return hasRel(LinkRelation.of(rel));
	}

	/**
	 * Returns whether the {@link Link} has the given {@link LinkRelation}.
	 *
	 * @param rel must not be {@literal null}.
	 * @return
	 */
	public boolean hasRel(LinkRelation rel) {
		return this.rel.isSameAs(Objects.requireNonNull(rel));
	}

	/**
	 * Returns the current href as URI after expanding the links without any
	 * arguments, i.e. all optional URI {@link TemplateVariable}s will be dropped.
	 * If the href contains mandatory {@link TemplateVariable}s, the URI creation
	 * will fail with an {@link IllegalStateException}.
	 *
	 * @return will never be {@literal null}.
	 */
	public URI toUri() {
		return URI.create(href);
	}

	/**
	 * Factory method to easily create {@link Link} instances from RFC-8288
	 * compatible {@link String} representations of a link.
	 *
	 * @param source an RFC-8288 compatible representation of a link.
	 * @throws IllegalArgumentException if a {@link String} was given that does not
	 *                                  adhere to RFC-8288.
	 * @throws IllegalArgumentException if no {@code rel} attribute could be found.
	 * @return will never be {@literal null}.
	 */
	public static Link valueOf(String source) {
		return LinkParser.parseLink(source, new int[] { 0 });
	}

	/**
	 * Create a new {@link Link} by copying all attributes and applying the new
	 * {@literal href}.
	 *
	 * @param href can be {@literal null}
	 * @return will never be {@literal null}.
	 */
	public Link withHref(String href) {

		return this.href == href ? this
				: new Link(this.rel, href, this.hreflang, this.media, this.title, this.type, this.profile, this.name);
	}

	/**
	 * Create a new {@link Link} by copying all attributes and applying the new
	 * {@literal hreflang}.
	 *
	 * @param hreflang can be {@literal null}
	 * @return will never be {@literal null}.
	 */
	public Link withHreflang( String hreflang) {

		return this.hreflang == hreflang ? this
				: new Link(this.rel, this.href, hreflang, this.media, this.title, this.type, this.profile, this.name);
	}

	/**
	 * Create a new {@link Link} by copying all attributes and applying the new
	 * {@literal media}.
	 *
	 * @param media can be {@literal null}
	 * @return will never be {@literal null}.
	 */
	public Link withMedia( String media) {

		return this.media == media ? this
				: new Link(this.rel, this.href, this.hreflang, media, this.title, this.type, this.profile, this.name);
	}

	/**
	 * Create a new {@link Link} by copying all attributes and applying the new
	 * {@literal title}.
	 *
	 * @param title can be {@literal null}
	 * @return will never be {@literal null}.
	 */
	public Link withTitle( String title) {

		return this.title == title ? this
				: new Link(this.rel, this.href, this.hreflang, this.media, title, this.type, this.profile, this.name);
	}

	/**
	 * Create a new {@link Link} by copying all attributes and applying the new
	 * {@literal type}.
	 *
	 * @param type can be {@literal null}
	 * @return will never be {@literal null}.
	 */
	public Link withType( String type) {

		return this.type == type ? this
				: new Link(this.rel, this.href, this.hreflang, this.media, this.title, type, this.profile, this.name);
	}

	/**
	 * Create a new {@link Link} by copying all attributes and applying the new
	 * {@literal profile}.
	 *
	 * @param profile can be {@literal null}
	 * @return will never be {@literal null}.
	 */
	public Link withProfile( String profile) {

		return this.profile == profile ? this
				: new Link(this.rel, this.href, this.hreflang, this.media, this.title, this.type, profile, this.name);
	}

	/**
	 * Create a new {@link Link} by copying all attributes and applying the new
	 * {@literal name}.
	 *
	 * @param name can be {@literal null}
	 * @return will never be {@literal null}.
	 */
	public Link withName( String name) {

		return this.name == name ? this
				: new Link(this.rel, this.href, this.hreflang, this.media, this.title, this.type, this.profile, name);
	}

	public LinkRelation getRel() {
		return this.rel;
	}

	public String getHref() {
		return this.href;
	}

	

	public Optional<String> getHreflang() {
		return Optional.of(hreflang);
	}

	

	public Optional<String> getMedia() {
		return Optional.of(this.media);
	}

	

	public Optional<String> getTitle() {
		return Optional.of(this.title);
	}

	

	public Optional<String> getType() {
		return Optional.of(this.type);
	}

	
	public Optional<String> getProfile() {
		return Optional.of(this.profile);
	}

	

	public Optional<String> getName() {
		return Optional.of(this.name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Link link = (Link) o;
		return Objects.equals(this.rel, link.rel) && Objects.equals(this.href, link.href)
				&& Objects.equals(this.hreflang, link.hreflang) && Objects.equals(this.media, link.media)
				&& Objects.equals(this.title, link.title) && Objects.equals(this.type, link.type)
				&& Objects.equals(this.profile, link.profile) && Objects.equals(this.name, link.name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		return Objects.hash(this.rel, this.href, this.hreflang, this.media, this.title, this.type, this.profile,
				this.name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		var result = new StringBuilder(64);

		result.append('<')
				// We only url-encode the `>`. We expect other special chars to already be
				// escaped. `;` and `,` need not
				// be escaped within the URL
				.append(href.replace(">", "%3e")).append(">;rel=");

		quoteParamValue(rel.value(), result);

		if (hreflang != null) {
			result.append(";hreflang=");
			quoteParamValue(hreflang, result);
		}

		if (media != null) {
			result.append(";media=");
			quoteParamValue(media, result);
		}

		if (title != null) {
			result.append(";title=");
			quoteParamValue(title, result);
		}

		if (type != null) {
			result.append(";type=");
			quoteParamValue(type, result);
		}

		if (profile != null) {
			result.append(";profile=");
			quoteParamValue(profile, result);
		}

		if (name != null) {
			result.append(";name=");
			quoteParamValue(name, result);
		}

		return result.toString();
	}

	/**
	 * Quotes the given string `s` and appends the result to the `target`. This
	 * method appends the start quote, the escaped text, and the end quote.
	 *
	 * @param s      Text to quote
	 * @param target StringBuilder to append to
	 */
	private static void quoteParamValue(String s, StringBuilder target) {

		// we reserve extra 4 chars: two for the start and end quote, another two are a
		// reserve for potential escaped chars
		target.ensureCapacity(target.length() + s.length() + 4);
		target.append('"');

		for (int i = 0, l = s.length(); i < l; i++) {

			char ch = s.charAt(i);

			if (ch == '"' || ch == '\\') {
				target.append('\\');
			}

			target.append(ch);
		}

		target.append('"');
	}

}
