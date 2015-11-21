import xml.etree.ElementTree as et
import sys

def run(rev):
	tree = et.parse('atmss_log')
	root = tree.getroot()
	lst = []

	for record in root.findall('record'):
		log = record.find('date').text + " | [" + record.find('level').text + "], class: " + record.find('class').text + ", method: " + record.find('method').text + ", msg: " + record.find('message').text
		tup = (int(record.find('sequence').text), log)
		lst.append(tup)

	lst.sort(key=lambda tup: tup[0], reverse = rev)		
	for record in lst:
		print("#" + str(record[0]) + " | " + record[1])

if __name__ == '__main__':
	reverse = False
	if "r" in sys.argv:
		reverse = True
	run(reverse)