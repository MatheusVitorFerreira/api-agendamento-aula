resource "aws_security_group" "contactagenda_sg" {
  name = "contactagenda_sg"
  description = "contactagenda security group"
  vpc_id = aws_vpc.contact_vpc_1.id
}

resource "aws_security_group_rule" "sgr_pub_out" {
  from_port         = 0
  protocol          = "-1"
  security_group_id = aws_security_group.contactagenda_sg.id
  to_port           = 0
  type              = "egress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "sgr_ssh_in" {
  from_port         = 22
  protocol          = "tcp"
  security_group_id = aws_security_group.contactagenda_sg.id
  to_port           = 22
  type              = "ingress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "srg_htpps_in" {
  from_port         = 443
  protocol          = "tcp"
  security_group_id = aws_security_group.contactagenda_sg.id
  to_port           = 443
  type              = "ingress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_key_pair" "contact_key" {
  key_name = "contact_key2"
  public_key = file("~/.ssh/contactagenda_key2.pub")
}

resource aws_instance "contact_ec2" {
  instance_type = ""
}