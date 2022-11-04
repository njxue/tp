package seedu.taassist.model.moduleclass;

import static java.util.Objects.requireNonNull;
import static seedu.taassist.commons.util.AppUtil.checkArgument;
import static seedu.taassist.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import seedu.taassist.model.session.Session;
import seedu.taassist.model.uniquelist.Identity;

/**
 * Represents a Class in TA-Assist.
 * Guarantees: immutable; name is valid as declared in {@link #isValidModuleClassName(String)}
 */
public class ModuleClass implements Identity<ModuleClass>, Comparable<ModuleClass> {

    public static final String MESSAGE_CONSTRAINTS = "Class name should be alphanumeric and"
            + " shouldn't exceed 25 characters.";
    public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    private final String className;

    private final List<Session> sessions;

    /**
     * Constructs a {@code ModuleClass}.
     *
     * @param className A valid class name.
     */
    public ModuleClass(String className) {
        requireNonNull(className);
        String upperCasedName = className.toUpperCase();
        checkArgument(isValidModuleClassName(upperCasedName), MESSAGE_CONSTRAINTS);
        this.className = upperCasedName;
        sessions = new ArrayList<Session>();
    }

    /**
     * Constructs a {@code ModuleClass} with the provided list of {@code Session}-s.
     *
     * @param className A valid class name.
     * @param sessions A list of sessions.
     */
    public ModuleClass(String className, List<Session> sessions) {
        requireAllNonNull(className, sessions);
        String upperCasedName = className.toUpperCase();
        checkArgument(isValidModuleClassName(upperCasedName), MESSAGE_CONSTRAINTS);
        this.className = upperCasedName;
        this.sessions = sessions;
    }

    /**
     * Returns true if a given string is a valid class name.
     */
    public static boolean isValidModuleClassName(String className) {
        return className.matches(VALIDATION_REGEX) && className.length() <= 25;
    }

    public String getClassName() {
        return className;
    }

    /**
     * Returns an immutable sessions list, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Session> getSessions() {
        return Collections.unmodifiableList(sessions);
    }

    /**
     * Returns a new {@code ModuleClass} by adding the provided {@code Session} to this {@code ModuleClass}.
     * If the session already exists, this {@code ModuleClass} is returned.
     */
    public ModuleClass addSession(Session session) {
        requireNonNull(session);
        if (hasSession(session)) {
            return this;
        }
        List<Session> newSessions = new ArrayList<>(sessions);
        newSessions.add(session);
        Collections.sort(newSessions);
        return new ModuleClass(className, newSessions);
    }

    /**
     * Returns a new {@code ModuleClass} by removing the {@code session}.
     */
    public ModuleClass removeSession(Session session) {
        requireNonNull(session);
        List<Session> newSessions = sessions.stream().filter(s -> !s.isSame(session)).collect(Collectors.toList());
        return new ModuleClass(className, newSessions);
    }

    /**
     * Returns true if both modules have the same name and session list.
     * This defines a stronger notion of equality between two module classes.
     *
     * @param other the object to be compared to.
     * @return true if objects are equal.
     */
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ModuleClass // instanceof handles nulls
                && className.equals(((ModuleClass) other).className)
                && sessions.equals(((ModuleClass) other).sessions));
    }

    /**
     * Returns true if both modules have the same name.
     * This defines a weaker notion of equality between two module classes.
     *
     * @param other the module class to be compared to.
     * @return true if both modules have the same name.
     */
    @Override
    public boolean isSame(ModuleClass other) {
        return other == this
                || (other != null && className.equals(other.className));
    }

    public boolean hasSession(Session toCheck) {
        return sessions.stream().anyMatch(toCheck::isSame);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, sessions);
    }

    /**
     * Formats state as text for viewing.
     */
    public String toString() {
        return getClassName();
    }

    @Override
    public int compareTo(ModuleClass other) {
        return className.compareTo(other.className);
    }

}
