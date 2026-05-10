import math

G = 6.67e-11
planets_escape_velocities = {}
AU = 149597870.7
solar_system_data = {}
planet_data = {}

#step one
def calculate_planetary_escape_velocity(mass, radius):
    escape_velocity = math.sqrt(2 * G * mass / radius)
    return escape_velocity

def each_planet_escape_velocity(): 
    try:
        with open('Planetary_Data.txt', 'r') as file:
            lines = file.readlines()
    except FileNotFoundError:
        print("Error: 'Planetary_Data.txt' file not found.")
        exit()
    earth_mass = None
    for line in lines:
        line = line.strip()
        if not line:
            continue
        try:
            data = line.split(',')
            if len(data) < 2:
                print(f"Warning: Line '{line}' does not contain enough data.")
                continue
            first_part = data[0].strip()
            second_part = data[1].strip()
            if not ':' in first_part:
                print(f"Warning: Line '{line}' does not contain a ':'.")
                continue

            planet_name, planet_diameter = first_part.split(':')
            planet_name = planet_name.strip()
        
            if(planet_name == 'Earth'):
                earth_mass = second_part.replace('mass','').strip()
                earth_mass = earth_mass.replace('=','').strip()
                earth_mass = earth_mass.replace('kg','').strip()
                earth_mass = earth_mass.replace('^','**').strip()
                earth_mass = eval(earth_mass)
                earth_mass = float(earth_mass)
                break
                #print(f"{planet_name} mass: {earth_mass} kg")
        except Exception as e:
            print(f"Error processing line '{line}': {e}")
    if earth_mass is None:
        print("Error: Earth mass not found in the data.")
        exit()
    for line in lines:
        line = line.strip()
        if not line:
            continue
        try: 
            data = line.split(',')
            if len(data) < 2:
                print(f"Warning: Line '{line}' does not contain enough data.")
                continue
            first_part = data[0].strip()
            second_part = data[1].strip()
            if not ':' in first_part:
                print(f"Warning: Line '{line}' does not contain a ':'.")
                continue
            planet_name, planet_diameter = first_part.split(':')
            planet_name = planet_name.strip()
            planet_diameter = planet_diameter.strip().replace('diameter','')
            planet_diameter = planet_diameter.strip().replace('=','')
            planet_diameter = planet_diameter.strip().replace('km','')
            planet_diameter = float(planet_diameter.strip())*1000
            planet_radius = planet_diameter / 2
            #print(f"{planet_name} diameter: {planet_diameter} m")
            if(planet_name == 'Earth'):
                planet_mass = earth_mass
                planet_data[planet_name] = {
                    'diameter_meters': planet_diameter,
                    'radius_meters': planet_radius,
                    'mass_kg': planet_mass
                }
            else:
                planet_mass = second_part.replace('mass','').strip()
                planet_mass = planet_mass.replace('=','').strip()
                planet_mass = planet_mass.replace('kg','').strip()
                planet_mass = planet_mass.replace('Earths','').strip()
                planet_mass = float(planet_mass) * earth_mass
            planet_data[planet_name] = {
                'diameter_meters': planet_diameter,
                'radius_meters': planet_radius,
                'mass_kg': planet_mass
            }
            escape_velocity = calculate_planetary_escape_velocity(planet_mass, planet_radius)
            planets_escape_velocities[planet_name] = escape_velocity
            print(f"{planet_name} escape velocity: {escape_velocity} m/s")
        except Exception as e:
            print(f"Error processing line '{line}': {e}")

#step two
def calculate_time_rocket_to_escape_velocity(escape_velocity, acceleration):
    time = escape_velocity / acceleration
    return time

def calculate_distance_rocket_to_escape_velocity(time, acceleration):
    v_initial = 0
    distance = v_initial * time + (acceleration * (time ** 2)) / 2
    return distance

def each_planet_time_and_distance_to_escape_velocity():
    try:
        with open('Rocket_Data.txt', 'r') as file:
            lines = file.readlines()
    except FileNotFoundError:
        print("Error: 'Rocket_Data.txt' file not found.")
        exit()
    acceleration_per_engine = None
    number_of_rocket_engines = None
    for line in lines:
        line = line.strip()
        if not line:
            continue
        try:
            parts = line.split(':')
            if len(parts) < 2:
                print(f"Warning: Line '{line}' does not contain enough data.")
                continue
            text_part = parts[0].strip()
            value_part = parts[1].strip()
            if 'number of rocket engines' in text_part.lower():
                number_of_rocket_engines = int(value_part)
            elif 'acceleration per engine' in text_part.lower():
                acceleration_per_engine = float(value_part.replace('m/s^2','').strip())
            else:
                print(f"Warning: Line '{line}' does not contain recognized data.")
        except Exception as e:
            print(f"Error processing line '{line}': {e}")
    if acceleration_per_engine is None or number_of_rocket_engines is None:
        print("Error: Required rocket data not found in the file.")
        exit()
    total_acceleration = acceleration_per_engine * number_of_rocket_engines
    for planet, escape_velocity in planets_escape_velocities.items():
        time_to_escape_velocity = calculate_time_rocket_to_escape_velocity(escape_velocity, total_acceleration)
        distance_to_escape_velocity = calculate_distance_rocket_to_escape_velocity(time_to_escape_velocity, total_acceleration)
        print(f"{planet} time to escape velocity: {time_to_escape_velocity} seconds, distance to escape velocity: {distance_to_escape_velocity} meters")
    return total_acceleration

#stage three
def read_solar_system_data():
    try:
        with open('Solar_System_Data.txt', 'r') as file:
            lines = file.readlines()
    except FileNotFoundError:
        print("Error: 'Planetary_Data.txt' file not found.")
        exit()
    for line in lines:
        line = line.strip()
        if not line:
            continue
        try:
            data = line.split(',')
            if len(data) < 2:
                print(f"Warning: Line '{line}' does not contain enough data.")
                continue
            first_part = data[0].strip()
            second_part = data[1].strip()
            if not ':' in first_part:
                print(f"Warning: Line '{line}' does not contain a ':'.")
                continue
            planet_name, planet_period = first_part.split(':')
            planet_name = planet_name.strip()
            planet_period = planet_period.strip().replace('period','')
            planet_period = planet_period.strip().replace('=','')
            planet_period = planet_period.strip().replace('days','')
            planet_period = float(planet_period.strip())
            planet_period_seconds = planet_period * 24 * 3600
            planet_orbital_radius = second_part.strip().replace('orbital radius','')
            planet_orbital_radius = planet_orbital_radius.strip().replace('=','')
            planet_orbital_radius = planet_orbital_radius.strip().replace('AU','')
            planet_orbital_radius = float(planet_orbital_radius.strip()) * AU * 1000
            planet_data[planet_name].update({
                'period_days': planet_period,
                'period_seconds': planet_period_seconds,
                'orbital_radius_AU': planet_orbital_radius / AU,
                'orbital_radius_meters': planet_orbital_radius
            })
        except Exception as e:
            print(f"Error processing line '{line}': {e}")


def data_of_travelling_between_2_planets(planet1, planet2, total_acceleration):
    planet1 = planet1.strip().capitalize()
    planet2 = planet2.strip().capitalize()
    if planet1 not in planet_data or planet2 not in planet_data:
        print(f"Error: Data for {planet1} or {planet2} not found.")
        return

    escape_velocity_planet1 = planets_escape_velocities[planet1]
    escape_velocity_planet2 = planets_escape_velocities[planet2]

    cruising_velocity = max(escape_velocity_planet1, escape_velocity_planet2)

    time_to_cruising_velocity = calculate_time_rocket_to_escape_velocity(cruising_velocity, total_acceleration)

    distance_from_surface_to_cruising_velocity = calculate_distance_rocket_to_escape_velocity(time_to_cruising_velocity, total_acceleration)

    distance_planet1_planet2 = abs(planet_data[planet1]['orbital_radius_meters'] - planet_data[planet2]['orbital_radius_meters'])

    d_cruise = (distance_planet1_planet2 - planet_data[planet1]['radius_meters'] - planet_data[planet2]['radius_meters']
        - distance_from_surface_to_cruising_velocity - distance_from_surface_to_cruising_velocity)

    if d_cruise < 0:
        print("Error: Cruise distance is negative.")
        return

    time_normal_velocity = d_cruise / cruising_velocity

    distance_when_deceleration_starts = distance_from_surface_to_cruising_velocity
    time_to_decelerate = time_to_cruising_velocity

    total_time = time_to_cruising_velocity + time_normal_velocity + time_to_decelerate
    total_seconds_int = int(round(total_time))
    days = total_seconds_int // 86400
    hours = (total_seconds_int % 86400) // 3600
    minutes = (total_seconds_int % 3600) // 60
    seconds = total_seconds_int % 60

    print(f"Data for travelling from {planet1} to {planet2}:")
    print(f"-> Rocket to reach cruising velocity: {time_to_cruising_velocity:.2f} seconds")
    print(f"-> Distance from {planet1}'s surface to cruising velocity: {distance_from_surface_to_cruising_velocity:.2f} meters")
    print(f"-> Time at normal velocity: {time_normal_velocity:.2f} seconds")
    print(f"-> Distance from {planet2}'s surface when deceleration starts: {distance_when_deceleration_starts:.2f} meters")
    print(f"-> Time to decelerate to 0 m/s when reaching {planet2}: {time_to_decelerate:.2f} seconds")
    print(f"-> Total travel time: {days} days, {hours} hours, {minutes} minutes, {seconds} seconds")

#stage four
def angular_position_of_planet(planet, input_value):
    planet = planet.capitalize()
    if planet not in planet_data:
        print(f"Error: Data for {planet} not found")
        return
    period_days = planet_data[planet]['period_days']
    remainder = input_value % period_days
    if abs(remainder) < 1e-9 or abs(remainder - period_days) < 1e-9:
        remainder = 0.0
    angular_position = (360.0 / period_days) * remainder
    angular_position = angular_position % 360.0
    if abs(angular_position) < 1e-9:
        angular_position = 0.0
    return angular_position

#stage five
def planet_position(planet, days):
    planet = planet.capitalize()
    if planet not in planet_data:
        print(f"Error: Data for {planet} not found")
        return
    radius = planet_data[planet]['orbital_radius_meters']
    theta = math.radians(angular_position_of_planet(planet, days))
    x = radius * math.cos(theta)
    y = radius * math.sin(theta)
    return (x, y)

def distance_between_planets(planet1, planet2, days):
    if planet1 not in planet_data or planet2 not in planet_data:
        print(f"Error: Data for {planet1} or {planet2} not found.")
        return
    pos1 = planet_position(planet1, days)
    pos2 = planet_position(planet2, days)
    if pos1 is None or pos2 is None:
        print("Error: Could not calculate positions for the planets.")
        return
    x1, y1 = pos1
    x2, y2 = pos2
    distance = math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)
    return distance

def center_of_every_planet():
    for planet, data in planet_data.items():
        radius = data['radius_meters']
        theta = math.radians(angular_position_of_planet(planet, 0))
        x = radius * math.cos(theta)
        y = radius * math.sin(theta)
        planet_data[planet].update({
            'center_x': x,
            'center_y': y
        })

def distance_point_to_a_segment(px, py, x1, y1, x2, y2):
    line_mag = math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)
    if line_mag < 1e-10:
        return math.sqrt((px - x1) ** 2 + (py - y1) ** 2)
    u = ((px - x1) * (x2 - x1) + (py - y1) * (y2 - y1)) / (line_mag ** 2)
    if u < 0:
        closest_x = x1
        closest_y = y1
    elif u > 1:
        closest_x = x2
        closest_y = y2
    else:
        closest_x = x1 + u * (x2 - x1)
        closest_y = y1 + u * (y2 - y1)
    distance = math.sqrt((px - closest_x) ** 2 + (py - closest_y) ** 2)
    return distance

def clear_direction(planet1, planet2, days):
    if planet1 not in planet_data or planet2 not in planet_data:
        print(f"Error: Data for {planet1} or {planet2} not found.")
        return
    pos1 = planet_position(planet1, days)
    pos2 = planet_position(planet2, days)
    if pos1 is None or pos2 is None:
        print("Error: Could not calculate positions for the planets.")
        return
    x1, y1 = pos1
    x2, y2 = pos2
    for planet, data in planet_data.items():
        if planet in [planet1, planet2]:
            continue
        px, py = planet_position(planet, days)
        radius = planet_data[planet]['radius_meters']
        distance = distance_point_to_a_segment(px, py, x1, y1, x2, y2)
        if distance < radius:
            return False
    return True

def find_first_optimal_transfer_window(planet1, planet2):
    planet1 = planet1.capitalize()
    planet2 = planet2.capitalize()
    if planet1 not in planet_data or planet2 not in planet_data:
        print(f"Error: Data for {planet1} or {planet2} not found.")
        return
    start_days = 100 * 365
    end_days = start_days + 10 * 365

    best_day = None
    best_distance = float('inf')
    print(f"Searching for optimal transfer window from {planet1} to {planet2} between day {start_days} and day {end_days}")
    print(f"Search period: {end_days - start_days} days")
    for day in range(start_days, end_days + 1):
        if not clear_direction(planet1, planet2, day):
            continue
        current_distance = distance_between_planets(planet1, planet2, day)
        if current_distance is not None and current_distance < best_distance:
            best_distance = current_distance
            best_day = day
    if best_day is None:
        print("No optimal transfer window found in the given range.")
        return
    wait_days = best_day - start_days
    wait_years = wait_days // 365
    remaining_days = wait_days % 365

    pos1 = planet_position(planet1, best_day)
    pos2 = planet_position(planet2, best_day)
    distance_km = best_distance / 1000

    print(f"Optimal transfer window found on day {best_day} (wait for {wait_years} years and {remaining_days} days).")

    #stage three prints
    escape_velocity_planet1 = planets_escape_velocities[planet1]
    escape_velocity_planet2 = planets_escape_velocities[planet2]

    cruising_velocity = max(escape_velocity_planet1, escape_velocity_planet2)

    time_to_cruising_velocity = calculate_time_rocket_to_escape_velocity(cruising_velocity, total_acceleration)

    distance_from_surface_to_cruising_velocity = calculate_distance_rocket_to_escape_velocity(time_to_cruising_velocity, total_acceleration)

    distance_planet1_planet2 = best_distance

    d_cruise = (distance_planet1_planet2 - planet_data[planet1]['radius_meters'] - planet_data[planet2]['radius_meters']
        - distance_from_surface_to_cruising_velocity - distance_from_surface_to_cruising_velocity)

    if d_cruise < 0:
        print("Error: Cruise distance is negative.")
        return

    time_normal_velocity = d_cruise / cruising_velocity

    distance_when_deceleration_starts = distance_from_surface_to_cruising_velocity
    time_to_decelerate = time_to_cruising_velocity

    total_time = time_to_cruising_velocity + time_normal_velocity + time_to_decelerate
    total_seconds_int = int(round(total_time))
    days = total_seconds_int // 86400
    hours = (total_seconds_int % 86400) // 3600
    minutes = (total_seconds_int % 3600) // 60
    seconds = total_seconds_int % 60

    print(f"Data for travelling from {planet1} to {planet2}:")
    print(f"-> Rocket to reach cruising velocity: {time_to_cruising_velocity:.2f} seconds")
    print(f"-> Distance from {planet1}'s surface to cruising velocity: {distance_from_surface_to_cruising_velocity:.2f} meters")
    print(f"-> Time at normal velocity: {time_normal_velocity:.2f} seconds")
    print(f"-> Distance from {planet2}'s surface when deceleration starts: {distance_when_deceleration_starts:.2f} meters")
    print(f"-> Time to decelerate to 0 m/s when reaching {planet2}: {time_to_decelerate:.2f} seconds")
    print(f"-> Total travel time: {days} days, {hours} hours, {minutes} minutes, {seconds} seconds")

    #stage four prints
    print(f"Planetary positions on day {best_day}:")
    for planet in planet_data.keys():
        angle = angular_position_of_planet(planet, best_day)
        print(f"{planet}: {angle:.2f} degrees")

#stage six
def rocket_position_at_time(pos1, pos2, t, total_time):
    if t < 0 or t > total_time:
        print("Error: Time t is out of bounds.")
        return None
    x1, y1 = pos1
    x2, y2 = pos2
    x = x1 + (x2 - x1) * (t / total_time)
    y = y1 + (y2 - y1) * (t / total_time)
    return (x, y)

def check_colision_with_planets(planet1, planet2, departure_day, total_time):
    step_time = 86400
    total_steps = int(total_time // step_time) + 1

    pos1 = planet_position(planet1, departure_day)
    pos2 = planet_position(planet2, departure_day)
    for step in range(total_steps):
        t = step * step_time
        current_day = departure_day + t / 86400

        rocket_pos = rocket_position_at_time(pos1, pos2, t, total_time)
        if rocket_pos is None:
            continue

        for planet, data in planet_data.items():
            if planet in [planet1, planet2]:
                continue
            px, py = planet_position(planet, current_day)
            radius = planet_data[planet]['radius_meters']
            distance_to_rocket = math.sqrt((rocket_pos[0] - px) ** 2 + (rocket_pos[1] - py) ** 2)
            if distance_to_rocket < radius:
                print(f"Collision detected with {planet} at time {t} seconds.")
                return False
    return True

def find_first_optimal_transfer_window_final(planet1, planet2):
    planet1 = planet1.capitalize()
    planet2 = planet2.capitalize()
    if planet1 not in planet_data or planet2 not in planet_data:
        print(f"Error: Data for {planet1} or {planet2} not found.")
        return
    start_days = 100 * 365
    end_days = start_days + 10 * 365

    escape_velocity_planet1 = planets_escape_velocities[planet1]
    escape_velocity_planet2 = planets_escape_velocities[planet2]

    cruising_velocity = max(escape_velocity_planet1, escape_velocity_planet2)

    time_to_cruising_velocity = calculate_time_rocket_to_escape_velocity(cruising_velocity, total_acceleration)

    distance_from_surface_to_cruising_velocity = calculate_distance_rocket_to_escape_velocity(time_to_cruising_velocity, total_acceleration)

    best_day = None
    best_distance = float('inf')
    print(f"Searching for optimal transfer window from {planet1} to {planet2} between day {start_days} and day {end_days}")
    print(f"Search period: {end_days - start_days} days")
    for day in range(start_days, end_days + 1):
        if not clear_direction(planet1, planet2, day):
            continue
        current_distance = distance_between_planets(planet1, planet2, day)

        d_cruise = (current_distance - planet_data[planet1]['radius_meters'] - planet_data[planet2]['radius_meters']
        - distance_from_surface_to_cruising_velocity - distance_from_surface_to_cruising_velocity)
        if d_cruise < 0:
            continue
        
        time_normal_velocity = d_cruise / cruising_velocity

        distance_when_deceleration_starts = distance_from_surface_to_cruising_velocity
        time_to_decelerate = time_to_cruising_velocity

        total_time = time_to_cruising_velocity + time_normal_velocity + time_to_decelerate

        safe_to_travel = check_colision_with_planets(planet1, planet2, day, total_time)
        if not safe_to_travel:
            continue
        if current_distance is not None and current_distance < best_distance:
            best_distance = current_distance
            best_day = day
    if best_day is None:
        print("No optimal transfer window found in the given range.")
        return
    wait_days = best_day - start_days
    wait_years = wait_days // 365
    remaining_days = wait_days % 365

    pos1 = planet_position(planet1, best_day)
    pos2 = planet_position(planet2, best_day)
    distance_km = best_distance / 1000

    print(f"Optimal transfer window found on day {best_day} (wait for {wait_years} years and {remaining_days} days) with no collision risk.")

    #stage three prints
    distance_planet1_planet2 = best_distance

    d_cruise = (distance_planet1_planet2 - planet_data[planet1]['radius_meters'] - planet_data[planet2]['radius_meters']
        - distance_from_surface_to_cruising_velocity - distance_from_surface_to_cruising_velocity)

    time_normal_velocity = d_cruise / cruising_velocity

    distance_when_deceleration_starts = distance_from_surface_to_cruising_velocity
    time_to_decelerate = time_to_cruising_velocity

    total_time = time_to_cruising_velocity + time_normal_velocity + time_to_decelerate
    
    total_seconds_int = int(round(total_time))
    days = total_seconds_int // 86400
    hours = (total_seconds_int % 86400) // 3600
    minutes = (total_seconds_int % 3600) // 60
    seconds = total_seconds_int % 60

    print(f"Data for travelling from {planet1} to {planet2}:")
    print(f"-> Rocket to reach cruising velocity: {time_to_cruising_velocity:.2f} seconds")
    print(f"-> Distance from {planet1}'s surface to cruising velocity: {distance_from_surface_to_cruising_velocity:.2f} meters")
    print(f"-> Time at normal velocity: {time_normal_velocity:.2f} seconds")
    print(f"-> Distance from {planet2}'s surface when deceleration starts: {distance_when_deceleration_starts:.2f} meters")
    print(f"-> Time to decelerate to 0 m/s when reaching {planet2}: {time_to_decelerate:.2f} seconds")
    print(f"-> Total travel time: {days} days, {hours} hours, {minutes} minutes, {seconds} seconds")

    #stage four prints
    print(f"Planetary positions on day {best_day}:")
    for planet in planet_data.keys():
        angle = angular_position_of_planet(planet, best_day)
        print(f"{planet}: {angle:.2f} degrees")


each_planet_escape_velocity()
#print(planet_data)
total_acceleration = each_planet_time_and_distance_to_escape_velocity()
read_solar_system_data()

start_planet = input("Enter starting planet : ")
destination_planet = input("Enter destination planet : ")
data_of_travelling_between_2_planets(start_planet, destination_planet, total_acceleration)

random_days = float(input("Enter random time (in days) : "))
print("Angular positions on that day:")
for planet in planet_data.keys():
    angle = angular_position_of_planet(planet, random_days)
    print(f"{planet}: {angle:.2f} degrees")

start_planet = input("Enter starting planet for transfer window : ")
destination_planet = input("Enter destination planet for transfer window : ")
find_first_optimal_transfer_window(start_planet, destination_planet)
find_first_optimal_transfer_window_final(start_planet, destination_planet)