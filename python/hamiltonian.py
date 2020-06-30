import time

eastern_states = ("mn", "ia", "mo", "ar", "la", "wi", "il", "tn", "ms", "mi", "in", "ky", "al",
                  "ga", "oh", "wv", "ny", "nj", "pa", "va", "nc", "sc", "fl", "me", "nh", "vt", "ma", "ct", "ri",
                  "de", "md", "wdc")

usa = {
    "wa": ["or", "id"],
    "or": ["wa", "id", "nv", "ca"],
    "ca": ["or", "nv", "az"],
    "id": ["wa", "or", "nv", "ut", "wy", "mt"],
    "nv": ["or", "ca", "az", "ut", "id"],
    "ut": ["id", "nv", "az", "co", "wy"],
    "az": ["ca", "nv", "ut", "nm"],
    "mt": ["id", "wy", "sd", "nd"],
    "wy": ["mt", "id", "ut", "co", "ne", "sd"],
    "co": ["wy", "ut", "nm", "ok", "ks", "ne"],
    "nm": ["co", "az", "tx", "ok"],
    "nd": ["mt", "sd", "mn"],
    "sd": ["nd", "mt", "wy", "ne", "ia", "mn"],
    "ne": ["sd", "wy", "co", "ks", "mo", "ia"],
    "ks": ["ne", "co", "ok", "mo"],
    "ok": ["ks", "co", "nm", "tx", "ar", "mo"],
    "tx": ["ok", "nm", "la", "ar"],
    "mn": ["nd", "sd", "ia", "wi"],
    "ia": ["mn", "sd", "ne", "mo", "il", "wi"],
    "mo": ["ia", "ne", "ks", "ok", "ar", "tn", "ky", "il"],
    "ar": ["mo", "ok", "tx", "la", "ms", "tn"],
    "la": ["ar", "tx", "ms"],
    "wi": ["mn", "ia", "il"],
    "il": ["wi", "ia", "mo", "ky", "in"],
    "tn": ["ky", "mo", "ar", "ms", "al", "ga", "nc", "va"],
    "ms": ["tn", "ar", "la", "al"],
    "mi": ["in", "oh"],
    "in": ["mi", "il", "ky", "oh"],
    "ky": ["oh", "in", "il", "mo", "tn", "va", "wv"],
    "al": ["tn", "ms", "fl", "ga"],
    "ga": ["nc", "tn", "al", "fl", "sc"],
    "oh": ["mi", "in", "ky", "wv", "pa"],
    "wv": ["pa", "oh", "ky", "va", "md"],
    "ny": ["pa", "nj", "ct", "ma", "vt"],
    "nj": ["ny", "pa", "de"],
    "pa": ["ny", "nj", "oh", "wv", "md", "de"],
    "va": ["md", "wv", "ky", "tn", "nc", "wdc"],
    "nc": ["va", "tn", "ga", "sc"],
    "sc": ["nc", "ga"],
    "fl": ["ga", "al"],
    "me": ["nh"],
    "nh": ["me", "vt", "ma"],
    "vt": ["nh", "ny", "ma"],
    "ma": ["nh", "vt", "ny", "ct", "ri"],
    "ct": ["ma", "ny", "ri"],
    "ri": ["ma", "ct"],
    "de": ["pa", "md", "nj"],
    "md": ["pa", "wv", "va", "de", "wdc"],
    "wdc": ["md", "va"]
}

eastern_usa = {}
journey = []


def clean_usa():
    for state in usa:
        if state in eastern_states:
            eastern_usa[state] = [eastern_state for eastern_state in usa[state] if eastern_state in eastern_states]
    return


def find_hamiltonian():
    if not hamiltonian_recursively('ri'):
        print('Could not find hamiltonian path.')


def hamiltonian_recursively(state):
    journey.append(state)
    if len(journey) == len(eastern_states):
        return True
    else:
        for neighbour in eastern_usa[state]:
            if neighbour not in journey:
                if hamiltonian_recursively(neighbour):
                    return True

    journey.pop()
    return False


clean_usa()

for _ in range(5):
    journey = []
    start = time.time()
    find_hamiltonian()
    end = time.time()
    print(' -> '.join(journey))
    print("Duration: " + str(end - start))
