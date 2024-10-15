resource "aws_vpc" "contact_vpc_1" {
  cidr_block = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support = true

  tags = {
    "name" = "contactagenda_vpc_1"
  }
}

resource "aws_subnet" "contact_subnet" {
  vpc_id                  = aws_vpc.contact_vpc_1.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true

  tags = {
    "Name" = "contactagenda_subnet_pub_1a"
  }
}

resource "aws_internet_gateway" "contactagenda_igw_1a" {
  vpc_id = aws_vpc.contact_vpc_1.id

  tags = {
    "Name" = "contactagenda_igw_1a"
  }
}

resource "aws_route_table" "contactagenda_rtb_pub" {
  vpc_id = aws_vpc.contact_vpc_1.id

  tags = {
    "Name" = "contactagenda_rtb_pub"
  }
}

resource "aws_route" "contactagenda_route" {
  route_table_id = aws_route_table.contactagenda_rtb_pub.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id = aws_internet_gateway.contactagenda_igw_1a.id
}

resource "aws_route_table_association" "contactagenda_rtb_ass" {
  route_table_id = aws_route_table.contactagenda_rtb_pub.id
  subnet_id = aws_subnet.contact_subnet.id
}
