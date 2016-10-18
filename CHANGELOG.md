# Version 2.1.0

## Breaking changes

SmartObject:

- `#objectType(type)` replaced by `.attribute("x_object_type", type)`
- `#registrationDate(date)` replaced by `.attribute("x_registration_date", date)`

Owner:

- `#username(username)` removed
- `#password(password)` removed
- `#registrationDate(date)` replaced by `.attribute("x_registration_date", date)`

The first two were ignored.
The third can be sent with: `.attribute("x_registration_date", date)`


Event:

- `#timestamp(date)` replaced by `.attribute("x_timestamp", date)`

A few public constants were also removed from these classes.