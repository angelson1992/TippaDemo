import time
import asyncio
import xmltodict
import cozmo
import urllib.request
import urllib.parse
from cozmo.objects import LightCube1Id, LightCube2Id, LightCube3Id
from PIL import Image, ImageDraw, ImageFont

def retreive_student_questions(email, password):
    data = {}
    data["email"] = email
    data["password"] = password
    data["request"] = "chooseQuestion"
    url_values = urllib.parse.urlencode(data)
    url = "http://localhost:9000/cozmo"
    full_url = url + "?" + url_values
    page = urllib.request.urlopen(full_url)
    data_dict = xmltodict.parse(page.read())

    structured_questions = {}
    
    for result in data_dict["sparql"]["results"]["result"]:
        current_course = ""
        current_understanding = ""
        current_question = ""
        current_possible_answer = ""
        current_correct_answer = ""
        for binding in result["binding"]:
            if(binding["@name"] == "courseLabel"):
                current_course = binding["literal"]
            if(binding["@name"] == "understanding"):
                current_understanding = binding["literal"]
            if(binding["@name"] == "question"):
                current_question = binding["literal"]["#text"]
            if(binding["@name"] == "possibleAnswer"):
                current_possible_answer = binding["literal"]["#text"]
            if(binding["@name"] == "correctAnswer"):
                current_correct_answer = binding["literal"]["#text"]

        #print("\n", current_course, "\n", current_understanding, "\n", current_question["#text"], "\n", current_possible_answer["#text"], "\n", current_correct_answer["#text"])

        if((current_course, current_understanding) not in structured_questions):
            structured_questions[(current_course, current_understanding)] = {}
            
        if(current_question not in structured_questions[(current_course, current_understanding)]):
            structured_questions[(current_course, current_understanding)][current_question] = {}
            
        structured_questions[(current_course, current_understanding)][current_question][current_possible_answer] = False

        structured_questions[(current_course, current_understanding)][current_question][current_correct_answer] = True

    return structured_questions

def construct_cozmo_quiz(robot, structured_quiz_data, isRandomized):
    chosen_course = ("", "11000")
    chosen_question = ""
    chosen_incorrect_answers = list()
    chosen_correct_answer = ""

    for key in structured_quiz_data:
        if(int(key[1]) < int(chosen_course[1])):
            chosen_course = (key[0], key[1])

    if(chosen_course in structured_quiz_data):
        for question in structured_quiz_data[chosen_course]:
            if(chosen_question == ""):
                chosen_question = question

    for answer in structured_quiz_data[chosen_course][chosen_question]:
        if(structured_quiz_data[chosen_course][chosen_question][answer]):
            chosen_correct_answer = answer
        else:
            chosen_incorrect_answers.append(answer)

    print(chosen_correct_answer)
    print(chosen_incorrect_answers)
    cozmo_ask_quiz_question_demo(robot, chosen_question, chosen_correct_answer, chosen_incorrect_answers[0], chosen_incorrect_answers[1])

def cozmo_lights(robot: cozmo.robot.Robot):
    cube1 = robot.world.get_light_cube(LightCube1Id)  # looks like a paperclip
    cube2 = robot.world.get_light_cube(LightCube2Id)  # looks like a lamp / heart
    cube3 = robot.world.get_light_cube(LightCube3Id)  # looks like the letters 'ab' over 'T'

    if cube1 is not None:
        cube1.set_lights(cozmo.lights.red_light)
    else:
        cozmo.logger.warning("Cozmo is not connected to a LightCube1Id cube - check the battery.")

    if cube2 is not None:
        cube2.set_lights(cozmo.lights.green_light)
    else:
        cozmo.logger.warning("Cozmo is not connected to a LightCube2Id cube - check the battery.")

    if cube3 is not None:
        cube3.set_lights(cozmo.lights.blue_light)
    else:
        cozmo.logger.warning("Cozmo is not connected to a LightCube3Id cube - check the battery.")

    # Keep the lights on for 10 seconds until the program exits
    time.sleep(10)

def make_text_image(text_to_draw, x, y, font=None):
    '''Make a PIL.Image with the given text printed on it

    Args:
        text_to_draw (string): the text to draw to the image
        x (int): x pixel location
        y (int): y pixel location
        font (PIL.ImageFont): the font to use

    Returns:
        :class:(`PIL.Image.Image`): a PIL image with the text drawn on it
    '''

    # make a blank image for the text, initialized to opaque black
    text_image = Image.new('RGBA', cozmo.oled_face.dimensions(), (0, 0, 0, 255))

    # get a drawing context
    dc = ImageDraw.Draw(text_image)

    # draw the text
    dc.text((x, y), text_to_draw, fill=(255, 255, 255, 255), font=font)

    return text_image

def text_to_face(robot, text):
    text_image = make_text_image(text, 0, 0, ImageFont.truetype("arial.ttf", 35))
    oled_face_data = cozmo.oled_face.convert_image_to_screen_data(text_image)
    return robot.display_oled_face_image(oled_face_data, 1000, in_parallel=True)

def cozmo_correct_answer_response(robot: cozmo.robot.Robot):
    action1 = robot.play_anim_trigger(cozmo.anim.Triggers.BuildPyramidThirdBlockUpright, in_parallel=True)
    
    cube1 = robot.world.get_light_cube(LightCube1Id)  # looks like a paperclip
    cube2 = robot.world.get_light_cube(LightCube2Id)  # looks like a lamp / heart
    cube3 = robot.world.get_light_cube(LightCube3Id)  # looks like the letters 'ab' over 'T'

    cube1.set_lights(cozmo.lights.green_light)
    cube2.set_lights(cozmo.lights.green_light)
    cube3.set_lights(cozmo.lights.green_light)

    time.sleep(0.1)

    cube1.set_lights(cozmo.lights.off_light)
    cube2.set_lights(cozmo.lights.off_light)
    cube3.set_lights(cozmo.lights.off_light)

    time.sleep(0.1)

    cube1.set_lights(cozmo.lights.green_light)
    cube2.set_lights(cozmo.lights.green_light)
    cube3.set_lights(cozmo.lights.green_light)

    time.sleep(0.1)

    action1.wait_for_completed()

    robot.say_text("Kor Rect", voice_pitch=0.6).wait_for_completed()

def cozmo_incorrect_answer_response(robot: cozmo.robot.Robot):
    action1 = robot.play_anim_trigger(cozmo.anim.Triggers.MajorFail, in_parallel=True)
    
    cube1 = robot.world.get_light_cube(LightCube1Id)  # looks like a paperclip
    cube2 = robot.world.get_light_cube(LightCube2Id)  # looks like a lamp / heart
    cube3 = robot.world.get_light_cube(LightCube3Id)  # looks like the letters 'ab' over 'T'

    cube1.set_lights(cozmo.lights.red_light)
    cube2.set_lights(cozmo.lights.red_light)
    cube3.set_lights(cozmo.lights.red_light)

    time.sleep(0.1)

    cube1.set_lights(cozmo.lights.off_light)
    cube2.set_lights(cozmo.lights.off_light)
    cube3.set_lights(cozmo.lights.off_light)

    time.sleep(0.1)

    cube1.set_lights(cozmo.lights.red_light)
    cube2.set_lights(cozmo.lights.red_light)
    cube3.set_lights(cozmo.lights.red_light)

    time.sleep(0.1)

    action1.wait_for_completed()

    robot.say_text("Try a gain", voice_pitch=0.8).wait_for_completed()

def cozmo_ask_quiz_question_demo(robot: cozmo.robot.Robot, question, correct_answer, incorrect_answer_1, incorrect_answer_2):
    cube1 = robot.world.get_light_cube(LightCube1Id)  # looks like a paperclip
    cube2 = robot.world.get_light_cube(LightCube2Id)  # looks like a lamp / heart
    cube3 = robot.world.get_light_cube(LightCube3Id)  # looks like the letters 'ab' over 'T'

    Cubes = [cube1, cube2, cube3]

    robot.say_text(question, voice_pitch=0.8).wait_for_completed()

    robot.say_text("Is it", voice_pitch=0.8).wait_for_completed()
    answer1_action_1 = robot.say_text(incorrect_answer_1, voice_pitch=0.8, in_parallel=True)
    answer1_action_2 = text_to_face(robot, incorrect_answer_1)
    cube1.set_lights(cozmo.lights.blue_light)
    answer1_action_1.wait_for_completed()
    answer1_action_2.wait_for_completed()
    cube1.set_lights(cozmo.lights.off_light)

    robot.say_text("Is it", voice_pitch=0.8).wait_for_completed()
    answer2_action_1 = robot.say_text(correct_answer, voice_pitch=0.8, in_parallel=True)
    answer2_action_2 = text_to_face(robot, correct_answer)
    cube2.set_lights(cozmo.lights.blue_light)
    answer2_action_1.wait_for_completed()
    answer2_action_2.wait_for_completed()
    cube2.set_lights(cozmo.lights.off_light)

    robot.say_text("Or is it", voice_pitch=0.8).wait_for_completed()
    answer3_action_1 = robot.say_text(incorrect_answer_2, voice_pitch=0.8, in_parallel=True)
    answer3_action_2 = text_to_face(robot, incorrect_answer_2)
    cube3.set_lights(cozmo.lights.blue_light)
    answer3_action_1.wait_for_completed()
    answer3_action_2.wait_for_completed()
    cube3.set_lights(cozmo.lights.off_light)

    last_cube1_tap = cube1.last_tapped_robot_timestamp
    last_cube2_tap = cube2.last_tapped_robot_timestamp
    last_cube3_tap = cube3.last_tapped_robot_timestamp

    while(not (cube1.last_tapped_robot_timestamp != last_cube1_tap or cube2.last_tapped_robot_timestamp != last_cube2_tap or cube3.last_tapped_robot_timestamp != last_cube3_tap)):
        time.sleep(0.0001)

    for cube in Cubes:
        if(cube.last_tapped_robot_timestamp):
            if(cube == cube2):
                cozmo_correct_answer_response(robot)
            else:
                cozmo_incorrect_answer_response(robot)

def begin_demo(robot: cozmo.robot.Robot):
    data = retreive_student_questions("johndoe2020@gmail.com", "passpass")
    construct_cozmo_quiz(robot, data, False)

cozmo.run_program(begin_demo)
