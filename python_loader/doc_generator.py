from faker import Faker

class BasicDocument:
    def __init__(self):
        self.first_name = None
        self.middle_name = None
        self.last_name = None
        self.address = {
            'first_line' : None,
            'second_line' : None,
            'city': None,
            'pin_code': None
            }
        self.phone_number = None
        self.birthday = None

    def __int__(self, document:dict):
        for key in document.keys():
            setattr(self, key, document[key])


class GenerateDocument:
    def __init__(self, seed):
        self.faker = Faker()
        self.faker.seed_instance(seed)

    def first_name(self):
        return self.faker.first_name()

    def middle_name(self):
        return self.faker.first_name()

    def last_name(self):
        return self.faker.last_name()

    def first_line(self):
        return self.faker.building_number()

    def second_line(self):
        return self.faker.street_name()

    def city(self):
        return self.faker.city()

    def pin_code(self):
        return self.faker.postcode()

    def phone_number(self):
        return self.faker.msisdn()

    def birthday(self):
        return self.faker.date_of_birth(
            minimum_age=5, maximum_age=100).strftime('%d-%m-%Y')

    fun_lookup = {
        'first_name' : first_name,
        'middle_name' : middle_name,
        'last_name' : last_name,
        'first_line' : first_line,
        'second_line' : second_line,
        'city' : city,
        'pin_code' : pin_code,
        'phone_number' : phone_number,
        'birthday' : birthday
        }

    def generate_documents(self, count):
        documents = []
        for i in range(count):
            document = BasicDocument()
            document.first_name = self.faker.first_name()
            document.middle_name = self.faker.first_name()
            document.last_name = self.faker.last_name()
            document.address['first_line'] = self.faker.building_number()
            document.address['second_line'] = self.faker.street_name()
            document.address['city'] = self.faker.city()
            document.address['pin_code'] = self.faker.postcode()
            document.phone_number = self.faker.msisdn()
            document.birthday = self.faker.date_of_birth(
                minimum_age=5, maximum_age=100).strftime('%d-%m-%Y')
            documents.append(document.__dict__)
        return documents

    def update_document(self, document:dict, count:int):
        keys = [*document]
        for i in range(count):
            key_index = self.faker.pyint(max_value=keys.__len__())
            key = keys[key_index]
            if key == "address":
                address_keys = [*document['address']]
                key_index = self.faker.pyint(
                    max_value=address_keys.__len__())
                address_key = address_keys[key_index]
                func = self.fun_lookup[address_key]
                document['address'][address_key] = func()
            else:
                func = self.fun_lookup[key]
                document[key] = func()
        return document

    def compare_documents(self, document1:dict, document2:dict):
        for key in document1.keys():
            if key not in document2:
                return False
            if isinstance(document1[key], dict):
                if isinstance(document2[key], dict):
                    same = self.compare_documents(document1[key],
                                                  document2[key])
                    if not same:
                        return False
                else:
                    return False
            if document1[key] != document2[key]:
                return False
        return True










